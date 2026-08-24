import { useEffect, useState } from 'react';
import { Button, Column, Form, Grid, InlineNotification, Loading, Modal, ProgressIndicator, ProgressStep, Stack } from '@carbon/react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { ArrowRight, TrashCan } from '@carbon/icons-react';
import { TENURED_OPENING, GOV_FUNDED_OPENING } from '@/constants';
import { scrollToSection } from '@/utils/InputUtils';
import PageTitle from '@/components/PageTitle';
import FeatureUnavailable from '@/components/FeatureUnavailable';
import {
  CreateOpeningStepOne as StepOne,
  CreateOpeningStepTwo as StepTwo,
  CreateOpeningStepThree as StepThree
} from '@/components/CreateOpeningSteps';
import ModalHead from '@/components/Modals/ModalHead';
import { CreateOpeningRoute, OpeningsRoute } from '@/routes/config';
import { OPENING_CREATE_SUCCESS_PATH } from '@/routes/paths';
import { useAuth } from '@/contexts/AuthProvider';
import { hasCreateOpeningPriviledge } from '@/utils/famUtils';

import { CreateOpeningFormType } from './definitions';
import { DefaultOpeningForm } from './constants';
import { validateStepOne, validateStepTwo } from './utils';
import { useMutation } from '@tanstack/react-query';
import API from '@/services/API';
import { ApiError, CreateOpeningRequestDto, TenureRequestDto, TenureValidationResponseDto } from '@/services/OpenApi';
import { sortValidatedTenures } from '@/utils/TenureUtils';

import './styles.scss';


const CreateOpening = () => {
  const [searchParams] = useSearchParams();
  const auth = useAuth();
  const [currentStep, setCurrentStep] = useState<number>(0);
  const [isCancelModalOpen, setIsCancelModalOpen] = useState<boolean>(false);
  const navigate = useNavigate();
  const type = searchParams.get('type');
  const [form, setForm] = useState<CreateOpeningFormType>(() => {
    return structuredClone(DefaultOpeningForm);
  });
  const [warnText, setWarnText] = useState<string | undefined>();
  const [uploadError, setUploadError] = useState<string | undefined>();
  const [tenureValidationResult, setTenureValidationResult] = useState<TenureValidationResponseDto | null>(null);
  const [showNoPrimaryError, setShowNoPrimaryError] = useState(false);
  const [tenureFieldErrors, setTenureFieldErrors] = useState<Array<{ fileId?: boolean; cutBlock?: boolean }> | undefined>();

  const isGovFundedOpening = type === GOV_FUNDED_OPENING;
  const isValidType = type === TENURED_OPENING || isGovFundedOpening;

  useEffect(() => {
    document.title = `Create Opening - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  // Alert user if they try to leave via browser refresh or navigation
  useEffect(() => {
    const handleBeforeUnload = (e: BeforeUnloadEvent) => {
      e.preventDefault();
    };

    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => window.removeEventListener('beforeunload', handleBeforeUnload);
  }, []);

  useEffect(() => {
    if (!isValidType) {
      console.warn("Invalid opening type");
      navigate("/", { replace: true });
    }
  }, [isValidType, navigate]);

  if (!auth.user || !hasCreateOpeningPriviledge(auth.user.privileges)) {
    return (
      <Grid className='create-opening-grid default-grid'>
        <Column sm={4} md={8} lg={16} id="title-col">
          <PageTitle
            title="Create new opening"
            subtitle="Register an opening to cover licensee or ministry responsibilities"
          />
        </Column>
        <Column sm={4} md={8} lg={16}>
          <InlineNotification
            lowContrast
            kind="warning"
            title="Insufficient privileges"
            subtitle="You do not have permission to create an opening."
          />
        </Column>
      </Grid>
    );
  }

  if (isGovFundedOpening) {
    return (
      <Grid className='create-opening-grid default-grid'>
        <Column sm={4} md={8} lg={16} id="title-col">
          <PageTitle
            title="Create new opening"
            breadCrumbs={[{ name: "Openings", path: OpeningsRoute.path! }, { name: "Create new opening", path: CreateOpeningRoute.path! }]}
          />
        </Column>

        <Column sm={4} md={8} lg={16}>
          <FeatureUnavailable
            featureName="Government funded opening"
            title="Government funded openings are unavailable"
            description="Creating government funded openings is not supported yet. Please create a tenure-based opening or return to the openings list."
            actionLabel="Back to openings"
            onActionClick={() => navigate(OpeningsRoute.path!)}
          />
        </Column>
      </Grid>
    );
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
  };

  const fileMutation = useMutation({
    mutationFn: (file: Blob) => API.OpeningCreateEndpointService.uploadOpeningSpatialFile({ file }),
    onSuccess: (data) => {
      setUploadError(undefined);
      setForm(f => ({
        ...f,
        file: f.file ? { ...f.file, validatedObj: data } : f.file,
        openingGrossArea: f.openingGrossArea
          ? {
            ...f.openingGrossArea,
            value: data?.geometryArea ? String(data.geometryArea) : f.openingGrossArea.value,
            isInvalid: false,
          }
          : f.openingGrossArea,
      }));
      scrollToSection(form.openingGrossArea?.id);
    },
    onError: (err) => {
      const status = err instanceof ApiError ? err.status : 0;
      if (status === 401) return;
      const body = err instanceof ApiError ? err.body : undefined;
      const message = body?.detail ?? body?.message;
      setForm(f => ({ ...f, file: f.file ? { ...f.file, validatedObj: undefined } : f.file }));
      setUploadError(message || 'File upload failed. Please try again.');
    }
  });

  const tenureValidationMutation = useMutation({
    mutationFn: (tenures: Array<TenureRequestDto>) =>
      API.TenureEndpointService.validateTenures(form.client?.value ?? '', tenures),
    onSuccess: (data) => {
      setWarnText(undefined);
      const sortedTenures = sortValidatedTenures(data.tenures);
      setForm(f => ({ ...f, tenureInfo: { ...f.tenureInfo, validatedTenures: sortedTenures } }));
      if (data.isValid) {
        setCurrentStep(2);
        scrollToSection('title-col');
      } else {
        setTenureValidationResult(data);
        scrollToSection('title-col');
      }
    },
    onError: (err) => {
      const status = err instanceof ApiError ? err.status : 0;
      if (status === 401) return;
      const body = err instanceof ApiError ? err.body : undefined;
      const message = body?.detail ?? body?.message;
      setWarnText(message || 'Tenure validation failed. Please try again.');
    },
  });

  /**
   * Build multipart payload for opening creation.
   * Form data at step 3 is already validated from steps 1 & 2,
   * so we trust the data integrity here.
   */
  const buildCreatePayload = () => {
    // Construct the CreateOpeningRequestDto
    const dto: CreateOpeningRequestDto = {
      clientNumber: form.client?.value ?? '',
      orgUnitCode: form.orgUnit?.value ?? '',
      openingCategoryCode: form.category?.value ?? '',
      licenseeOpeningId: form.licenseeOpeningId?.value || undefined,
      openingGrossArea: parseFloat(form.openingGrossArea?.value ?? '0'),
      maxAllowablePermAccessPerc: parseFloat(form.maxAllowablePermAccess?.value ?? '7'),
      tenures: form.tenureInfo?.value ?? [],
    };

    // Use the original uploaded file (not the processed GeoJSON).
    const fileBlob = form.file?.value;

    return { dto, fileBlob };
  };

  const createMutation = useMutation({
    mutationFn: async ({ dto, fileBlob }: { dto: CreateOpeningRequestDto; fileBlob?: Blob }) => {
      if (!fileBlob) {
        throw new Error('Spatial file is required for opening creation');
      }
      return API.OpeningCreateEndpointService.createOpening({
        data: dto,
        file: fileBlob,
      });
    },
    onSuccess: (data) => {
      setWarnText(undefined);
      navigate(`${OPENING_CREATE_SUCCESS_PATH}?openingId=${data.openingId}`);
    },
    onError: (err) => {
      const status = err instanceof ApiError ? err.status : 0;
      if (status === 401) return;

      const body = err instanceof ApiError ? err.body : undefined;
      const message = body?.detail ?? body?.message;

      // Map error status codes to user-friendly messages
      let userMessage = message || 'Creation failed. Please try again.';
      if (status === 400) {
        userMessage = `Form validation failed: ${message || 'Please check all fields.'}`;
      } else if (status === 403) {
        userMessage = 'Not authorized to create opening for this client.';
      } else if (status === 404) {
        userMessage = `Required resource not found: ${message || 'Check opening category and organization unit.'}`;
      } else if (status === 422) {
        userMessage = `File or coordinates invalid: ${message || 'Please verify your spatial file.'}`;
      } else if (status >= 500) {
        userMessage = 'Server error. Please try again or contact support.';
      }

      setWarnText(userMessage);
      scrollToSection('title-col');
    },
  });

  const handleBack = () => {
    if (currentStep === 0) {
      navigate(OpeningsRoute.path!)
    }
    setCurrentStep(s => Math.max(0, s - 1));
  }

  const handleNext = () => {
    if (currentStep === 0) {
      const { isValid, form: validatedForm } = validateStepOne(form);
      if (!isValid) {
        setForm(validatedForm);
        scrollToSection(DefaultOpeningForm.client?.id || 'opening-client-input');
        return;
      }

      setCurrentStep(1);
      scrollToSection('title-col');
      return;
    }

    if (currentStep === 1) {
      const tenures = form.tenureInfo?.value ?? [];
      const { isValid, hasPrimary, errors, trimmed } = validateStepTwo(tenures);

      if (!isValid) {
        setTenureFieldErrors(errors);
        setForm(f => ({ ...f, tenureInfo: { ...f.tenureInfo, value: trimmed } }));
        scrollToSection('title-col');
        return;
      }

      if (!hasPrimary) {
        setShowNoPrimaryError(true);
        setForm(f => ({ ...f, tenureInfo: { ...f.tenureInfo, value: trimmed } }));
        scrollToSection('title-col');
        return;
      }

      setShowNoPrimaryError(false);
      setTenureFieldErrors(undefined);
      setForm(f => ({ ...f, tenureInfo: { ...f.tenureInfo, value: trimmed } }));
      tenureValidationMutation.mutate(trimmed);
    }
  }

  const handleCancel = () => {
    setIsCancelModalOpen(true);
  }

  const handleCreate = () => {
    const payload = buildCreatePayload();
    createMutation.mutate(payload);
  }

  return (
    <Grid className='create-opening-grid default-grid'>
      <Column sm={4} md={8} lg={16} id="title-col">
        <PageTitle
          title="Create new opening"
          breadCrumbs={[{ name: "Openings", path: OpeningsRoute.path! }, { name: "Create new opening", path: `${CreateOpeningRoute.path!}?type=${type}`, current: true }]}
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <ProgressIndicator
          className='default-full-width-progress-indicator'
          currentIndex={currentStep}
        >
          <ProgressStep
            current={currentStep === 0}
            description="Opening information"
            label="Opening information"
          />
          <ProgressStep
            current={currentStep === 1}
            description="Tenure information"
            label="Tenure information"
          />
          <ProgressStep
            current={currentStep === 2}
            description="Review and create"
            label="Review and create"
          />
        </ProgressIndicator>
      </Column>

      {
        warnText
          ? (
            <Column sm={4} md={8} lg={16}>
              <InlineNotification lowContrast kind="warning" subtitle={warnText} onCloseButtonClick={() => setWarnText(undefined)} />
            </Column>
          )
          : null
      }

      <Column sm={4} md={8} lg={16}>
        <Form noValidate onSubmit={handleSubmit}>
          <Grid className="create-opening-form-grid">
            {
              currentStep === 0
                ? <StepOne
                  form={form}
                  setForm={setForm}
                  uploadError={uploadError}
                  onUploadErrorDismiss={() => setUploadError(undefined)}
                  onFileAdded={(f) => { setUploadError(undefined); fileMutation.mutate(f); }}
                  isUploading={fileMutation.isPending}
                />
                : null
            }
            {
              currentStep === 1
                ? <StepTwo
                  form={form}
                  setForm={setForm}
                  validationResult={tenureValidationResult}
                  showNoPrimaryError={showNoPrimaryError}
                  fieldErrors={tenureFieldErrors}
                  onTenuresChange={() => {
                    setTenureValidationResult(null);
                    setShowNoPrimaryError(false);
                    setTenureFieldErrors(undefined);
                  }}
                />
                : null
            }
            {
              currentStep === 2
                ? <StepThree form={form} setStep={setCurrentStep} />
                : null
            }
          </Grid>
        </Form>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="create-opening-button-grid">
          <Column sm={4} md={4}>
            {
              currentStep === 0
                ? (
                  <Button className="default-button" kind="secondary" onClick={handleCancel}>
                    Cancel
                  </Button>
                ) :
                (
                  <Button className="default-button" kind="secondary" onClick={handleBack}>
                    Previous
                  </Button>
                )
            }
          </Column>
          <Column sm={4} md={4}>
            {
              currentStep === 2
                ? (
                  <Button
                    className="default-button"
                    kind="primary"
                    onClick={handleCreate}
                    disabled={fileMutation.isPending || tenureValidationMutation.isPending || createMutation.isPending}
                    renderIcon={createMutation.isPending ? Loading : undefined}
                  >
                    Create new opening
                  </Button>
                ) :
                (
                  <Button className="default-button" kind="primary" onClick={handleNext} renderIcon={ArrowRight} disabled={fileMutation.isPending || tenureValidationMutation.isPending}>
                    Next
                  </Button>
                )
            }
          </Column>
        </Grid>
      </Column>

      <Modal
        passiveModal
        danger
        open={isCancelModalOpen}
        modalHeading={<ModalHead title="Are you sure you want to cancel?" helperTop="Create new opening" />}
        onRequestClose={() => setIsCancelModalOpen(false)}
        className="default-modal"
        preventCloseOnClickOutside
        size="sm"
      >
        <Grid>
          <Column sm={4} md={8} lg={16}>
            <p className='cancel-content'>
              If you leave this page, all the information you've entered will be lost.
            </p>
          </Column>
          <Column sm={4} md={8} lg={16}>
            <Stack orientation="horizontal" gap={2} className="default-equal-split-stack">
              <Button className="modal-button" kind="secondary" onClick={() => setIsCancelModalOpen(false)}>
                Continue reviewing
              </Button>

              <Button className="modal-button" kind="danger" renderIcon={TrashCan} onClick={() => navigate(OpeningsRoute.path!)}>
                Leave without saving
              </Button>
            </Stack>
          </Column>
        </Grid>
      </Modal>
    </Grid>
  );
};

export default CreateOpening;
