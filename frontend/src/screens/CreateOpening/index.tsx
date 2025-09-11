import { useEffect, useState } from 'react';
import { Button, Column, Form, Grid, InlineNotification, Modal, ProgressIndicator, ProgressStep, Stack } from '@carbon/react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { ArrowRight, Checkmark, TrashCan } from '@carbon/icons-react';
import { TENURED_OPENING, GOV_FUNDED_OPENING } from '@/constants';
import { OpeningTypes } from '@/types/OpeningTypes';
import { useAuth } from '@/contexts/AuthProvider';
import { scrollToSection } from '@/utils/InputUtils';
import PageTitle from '@/components/PageTitle';
import { CreateOpeningFileUpload, CreateOpeningForm } from '@/components/CreateOpeningSteps';
import { isRealNumber } from '@/utils/ValidationUtils';
import ModalHead from '@/components/Modals/ModalHead';
import { OpeningsRoute } from '@/routes/config';

import { CreateOpeningFormType } from './definitions';
import { DefaultOpeningForm, TitleText } from './constants';
import './styles.scss';

const CreateOpening = () => {
  const { selectedClient } = useAuth();
  const [searchParams] = useSearchParams();
  const [currentStep, setCurrentStep] = useState<number>(0);
  const [isCancelModalOpen, setIsCancelModalOpen] = useState<boolean>(false);
  const navigate = useNavigate();
  const type = searchParams.get('type');
  const [form, setForm] = useState<CreateOpeningFormType>(() => {
    const defaultForm = structuredClone(DefaultOpeningForm);
    return {
      ...defaultForm,
      client: {
        ...defaultForm.client,
        value: selectedClient,
      }
    }
  });
  const [warnText, setWarnText] = useState<string | undefined>();

  useEffect(() => {
    const isValidType = type === TENURED_OPENING || type === GOV_FUNDED_OPENING;

    if (!isValidType) {
      console.warn("Invalid opening type");
      navigate("/", { replace: true });
      return;
    }

    if (!selectedClient) {
      console.warn("No selected client");
      navigate("/", { replace: true });
    }

    if (form.client?.value && form.client.value !== selectedClient) {
      setWarnText("District office changed. The form will continue using the originally selected district.")
    };
  }, [type, selectedClient, navigate]);

  const openingType = type! as OpeningTypes;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
  };

  const handleBack = () => {
    if (currentStep === 0) {
      navigate(OpeningsRoute.path!)
    }
    setCurrentStep(s => Math.max(0, s - 1));
  }

  const handleNext = () => {
    if (currentStep === 0) {
      if (!form.geojson?.value) {
        setForm(
          (prev) => (
            { ...prev, isGeoJsonMissing: { ...prev.isGeoJsonMissing, value: true } }
          )
        )
        scrollToSection(form.client?.id)
        return;
      }

      setCurrentStep(1);
      return;
    }

    if (currentStep === 1 && validateForm()) {
      setCurrentStep(2);
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

    if (!validatedForm.orgUnit.value?.code) {
      isValid = false;
      validatedForm.orgUnit.isInvalid = true;
    }
    if (!validatedForm.category.value?.code) {
      isValid = false;
      validatedForm.category.isInvalid = true;
    }

    const openingGrossArea = validatedForm.openingGrossArea.value;
    if (!isRealNumber(openingGrossArea)) {
      isValid = false;
      validatedForm.openingGrossArea.isInvalid = true;
    }

    const maxAllowablePermAccess = validatedForm.maxAllowablePermAccess.value;
    if (!isRealNumber(maxAllowablePermAccess)) {
      isValid = false;
      validatedForm.maxAllowablePermAccess.isInvalid = true;
    }

    if (!validatedForm.tenureInfo.value || !validatedForm.tenureInfo.value.length) {
      isValid = false;
      validatedForm.tenureInfo.isInvalid = true;
    }

    if (!isValid) {
      console.log(validatedForm);
      setForm(validatedForm);
    }

    return isValid;
  }


  return (
    <Grid className='create-opening-grid default-grid'>
      <Column sm={4} md={8} lg={16}>
        <PageTitle
          title={
            `Create an opening: ${TitleText[openingType]}`
          }
          subtitle="Register an opening to cover licensee or ministry responsibilities"
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <ProgressIndicator
          className='default-full-width-progress-indicator'
          currentIndex={currentStep}
        >
          <ProgressStep
            current={currentStep === 0}
            description="File Upload"
            label="File Upload"
            secondaryLabel="Step 1"
          />
          <ProgressStep
            current={currentStep === 1}
            description="Opening Details"
            label="Opening Details"
            secondaryLabel="Step 2"
          />
          <ProgressStep
            current={currentStep === 2}
            description="Review & Create"
            label="Review & Create"
            secondaryLabel="Step 3"
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

      <Column sm={4} md={8} lg={16} xlg={12} max={10}>
        <Form noValidate onSubmit={handleSubmit}>
          <Grid className="create-opening-form-grid">
            {
              currentStep === 0
                ? <CreateOpeningFileUpload form={form} setForm={setForm} />
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
                  <Button className="default-button" kind="primary" onClick={handleNext} renderIcon={ArrowRight}>
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
        modalHeading={<ModalHead title="Are you sure you want to cancel?" helperTop={`Create an opening: ${TitleText[openingType]}`} />}
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
    </Grid >
  );
};

export default CreateOpening;
