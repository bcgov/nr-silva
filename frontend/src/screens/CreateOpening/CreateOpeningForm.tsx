import { FormEvent, useCallback, useEffect, useRef, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Button, Column, Form, Grid, InlineNotification, Loading, Modal, Stack } from '@carbon/react';
import { useBlocker, useNavigate } from 'react-router-dom';
import { ArrowRight, TrashCan } from '@carbon/icons-react';
import { scrollToSection } from '@/utils/InputUtils';
import {
  CreateOpeningStepOne as StepOne,
  CreateOpeningStepTwo as StepTwo,
  CreateOpeningStepThree as StepThree
} from '@/components/CreateOpeningSteps';
import LeavePageModal from '@/components/Modals/LeavePageModal';
import ModalHead from '@/components/Modals/ModalHead';
import { OpeningsRoute } from '@/routes/config';
import { OPENING_CREATE_SUCCESS_PATH } from '@/routes/paths';
import { CreateOpeningFormType } from './definitions';
import { DefaultOpeningForm } from './constants';
import { validateStepOne, validateStepTwo } from './utils';
import API from '@/services/API';
import { ApiError, CreateOpeningRequestDto, TenureRequestDto, TenureValidationResponseDto } from '@/services/OpenApi';
import { sortValidatedTenures } from '@/utils/TenureUtils';

interface CreateOpeningFormProps {
  type: string | null;
  currentStep: number;
  setCurrentStep: (step: number) => void;
}

export const CreateOpeningForm = ({ type, currentStep, setCurrentStep }: CreateOpeningFormProps) => {
  // All hooks called unconditionally at component top
  const [isCancelModalOpen, setIsCancelModalOpen] = useState<boolean>(false);
  const navigate = useNavigate();
  const [form, setForm] = useState<CreateOpeningFormType>(() => {
    return structuredClone(DefaultOpeningForm);
  });
  const [warnText, setWarnText] = useState<string | undefined>();
  const [uploadError, setUploadError] = useState<string | undefined>();
  const [tenureValidationResult, setTenureValidationResult] = useState<TenureValidationResponseDto | null>(null);
  const [showNoPrimaryError, setShowNoPrimaryError] = useState(false);
  const [tenureFieldErrors, setTenureFieldErrors] = useState<Array<{ fileId?: boolean; cutBlock?: boolean }> | undefined>();
  const [isNavigationBlocked, setIsNavigationBlocked] = useState(false);
  const bypassBlockerRef = useRef(false);
  const blocker = useBlocker(useCallback(() => isNavigationBlocked && !bypassBlockerRef.current, [isNavigationBlocked]));

  const handleSetCurrentStep = (stepOrUpdater: number | ((prev: number) => number)) => {
    const newStep = typeof stepOrUpdater === 'function' ? stepOrUpdater(currentStep) : stepOrUpdater;
    setCurrentStep(newStep);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
  };

  useEffect(() => {
    const hasStarted = currentStep !== 0 || JSON.stringify(form) !== JSON.stringify(DefaultOpeningForm);
    setIsNavigationBlocked(hasStarted);
  }, [currentStep, form]);

  const fileMutation = useMutation({
    mutationFn: (file: Blob) => API.OpeningCreateEndpointService.uploadOpeningSpatialFile({ file }),
    onSuccess: (data) => {
      setUploadError(undefined);
      setForm((f: CreateOpeningFormType) => ({
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
      setForm((f: CreateOpeningFormType) => ({ ...f, file: f.file ? { ...f.file, validatedObj: undefined } : f.file }));
      setUploadError(message || 'File upload failed. Please try again.');
    }
  });

  const tenureValidationMutation = useMutation({
    mutationFn: (tenures: Array<TenureRequestDto>) =>
      API.TenureEndpointService.validateTenures(form.client?.value ?? '', tenures),
    onSuccess: (data) => {
      setWarnText(undefined);
      const sortedTenures = sortValidatedTenures(data.tenures);
      setForm((f: CreateOpeningFormType) => ({ ...f, tenureInfo: { ...f.tenureInfo, validatedTenures: sortedTenures } }));
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
    const dto: CreateOpeningRequestDto = {
      clientNumber: form.client?.value ?? '',
      orgUnitCode: form.orgUnit?.value ?? '',
      openingCategoryCode: form.category?.value ?? '',
      licenseeOpeningId: form.licenseeOpeningId?.value || undefined,
      openingGrossArea: parseFloat(form.openingGrossArea?.value ?? '0'),
      maxAllowablePermAccessPerc: parseFloat(form.maxAllowablePermAccess?.value ?? '7'),
      tenures: form.tenureInfo?.value ?? [],
    };

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
      bypassBlockerRef.current = true;
      navigate(`${OPENING_CREATE_SUCCESS_PATH}?openingId=${data.openingId}`);
    },
    onError: (err) => {
      const status = err instanceof ApiError ? err.status : 0;
      if (status === 401) return;

      const body = err instanceof ApiError ? err.body : undefined;
      const message = body?.detail ?? body?.message;

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
    setCurrentStep(Math.max(0, currentStep - 1));
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
        setForm((f: CreateOpeningFormType) => ({ ...f, tenureInfo: { ...f.tenureInfo, value: trimmed } }));
        scrollToSection('title-col');
        return;
      }

      if (!hasPrimary) {
        setShowNoPrimaryError(true);
        setForm((f: CreateOpeningFormType) => ({ ...f, tenureInfo: { ...f.tenureInfo, value: trimmed } }));
        scrollToSection('title-col');
        return;
      }

      setShowNoPrimaryError(false);
      setTenureFieldErrors(undefined);
      setForm((f: CreateOpeningFormType) => ({ ...f, tenureInfo: { ...f.tenureInfo, value: trimmed } }));
      tenureValidationMutation.mutate(trimmed);
    }
  }

  const handleCancel = () => {
    setIsCancelModalOpen(true);
  };

  const handleLeaveConfirm = () => {
    bypassBlockerRef.current = true;
    blocker.proceed();
  };

  const handleStay = () => {
    blocker.reset();
  };

  const handleCreate = () => {
    const payload = buildCreatePayload();
    createMutation.mutate(payload);
  };

  return (
    <>
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
                ? <StepThree form={form} setStep={handleSetCurrentStep} />
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

              <Button className="modal-button" kind="danger" renderIcon={TrashCan} onClick={() => { bypassBlockerRef.current = true; navigate(OpeningsRoute.path!); }}>
                Leave without saving
              </Button>
            </Stack>
          </Column>
        </Grid>
      </Modal>

      <LeavePageModal
        open={blocker.state === 'blocked'}
        onRequestClose={handleStay}
        onLeave={handleLeaveConfirm}
        onStay={handleStay}
      />
    </>
  );
};
