import { useEffect, useState } from 'react';
import { Button, Column, Form, Grid, InlineNotification, Loading, Modal, ProgressIndicator, ProgressStep, Stack } from '@carbon/react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { ArrowRight, Checkmark, TrashCan } from '@carbon/icons-react';
import { TENURED_OPENING, GOV_FUNDED_OPENING } from '@/constants';
import { scrollToSection } from '@/utils/InputUtils';
import PageTitle from '@/components/PageTitle';
import FeatureUnavailable from '@/components/FeatureUnavailable';
import { CreateOpeningStepOne as StepOne, CreateOpeningForm } from '@/components/CreateOpeningSteps';
import { isRealNumber } from '@/utils/ValidationUtils';
import ModalHead from '@/components/Modals/ModalHead';
import { OpeningsRoute } from '@/routes/config';
import { useAuth } from '@/contexts/AuthProvider';
import { hasCreateOpeningPriviledge } from '@/utils/famUtils';

import { CreateOpeningFormType } from './definitions';
import { DefaultOpeningForm } from './constants';
import { validateStepOne } from './utils';
import { useMutation } from '@tanstack/react-query';
import API from '@/services/API';
import { ApiError } from '@/services/OpenApi';

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

  const isGovFundedOpening = type === GOV_FUNDED_OPENING;
  const isValidType = type === TENURED_OPENING || isGovFundedOpening;

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
            subtitle="Register an opening to cover licensee or ministry responsibilities"
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
      setForm(f => ({ ...f, file: f.file ? { ...f.file, validatedObj: data } : f.file }));
      setCurrentStep(1);
    },
    onError: (err) => {
      // Spring returns ProblemDetail with `detail`; older errors use `message`
      const body = err instanceof ApiError ? err.body : undefined;
      const message = body?.detail ?? body?.message;
      setForm(f => ({ ...f, file: f.file ? { ...f.file, validatedObj: undefined } : f.file }));
      setUploadError(message || 'File upload failed. Please try again.');
    }
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

      if (!form.file?.value) {
        scrollToSection(DefaultOpeningForm.file?.id || 'opening-map-file-drop-container');
        return;
      }

      setUploadError(undefined);
      fileMutation.mutate(form.file.value);

      return;
    }

    if (currentStep === 1 && validateForm()) {
      setCurrentStep(2);
      scrollToSection('title-col');
    }
  }

  const handleCancel = () => {
    setIsCancelModalOpen(true);
  }

  const handleCreate = () => {
    console.log(form);
  }

  function validateForm(): boolean {
    let isValid = true;
    const validatedForm = structuredClone(form);

    if (!validatedForm.orgUnit?.value) {
      isValid = false;
      if (validatedForm.orgUnit) {
        validatedForm.orgUnit.isInvalid = true;
      }
    }
    if (!validatedForm.category?.value) {
      isValid = false;
      if (validatedForm.category) {
        validatedForm.category.isInvalid = true;
      }
    }

    const openingGrossArea = validatedForm.openingGrossArea?.value;
    if (!isRealNumber(openingGrossArea)) {
      isValid = false;
      if (validatedForm.openingGrossArea) {
        validatedForm.openingGrossArea.isInvalid = true;
      }
    }

    const maxAllowablePermAccess = validatedForm.maxAllowablePermAccess?.value;
    if (!isRealNumber(maxAllowablePermAccess)) {
      isValid = false;
      if (validatedForm.maxAllowablePermAccess) {
        validatedForm.maxAllowablePermAccess.isInvalid = true;
      }
    }

    if (!validatedForm.tenureInfo?.value || !validatedForm.tenureInfo.value.length) {
      isValid = false;
      if (validatedForm.tenureInfo) {
        validatedForm.tenureInfo.isInvalid = true;
      }
    }

    if (!isValid) {
      console.log(validatedForm);
      setForm(validatedForm);
    }

    return isValid;
  }


  return (
    <Grid className='create-opening-grid default-grid'>
      <Column sm={4} md={8} lg={16} id="title-col">
        <PageTitle title="Create new opening" />
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
                ? <StepOne form={form} setForm={setForm} uploadError={uploadError} onUploadErrorDismiss={() => setUploadError(undefined)} />
                : null
            }
            {
              currentStep !== 0
                ? <CreateOpeningForm isReview={currentStep === 2} form={form} setForm={setForm} handleBack={handleBack} />
                : null
            }
          </Grid>
        </Form>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="create-opening-button-grid">
          <Column sm={4} md={4}>
            {
              currentStep === 2
                ? (
                  <Button className="default-button" kind="secondary" onClick={handleCancel}>
                    Cancel
                  </Button>
                ) :
                (
                  <Button className="default-button" kind="secondary" onClick={handleBack}>
                    Back
                  </Button>
                )
            }

          </Column>
          <Column sm={4} md={4}>
            {
              currentStep === 2
                ? (
                  <Button className="default-button" kind="primary" onClick={handleCreate} renderIcon={Checkmark}>
                    Create
                  </Button>
                ) :
                (
                  <Button className="default-button" kind="primary" onClick={handleNext} renderIcon={fileMutation.isPending ? Loading : ArrowRight} disabled={fileMutation.isPending}>
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
