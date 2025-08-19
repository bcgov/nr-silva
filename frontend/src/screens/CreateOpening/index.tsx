import { useEffect, useState } from 'react';
import { Button, Column, Form, Grid, InlineNotification, ProgressIndicator, ProgressStep } from '@carbon/react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { ArrowRight } from '@carbon/icons-react';
import { TENURED_OPENING, GOV_FUNDED_OPENING } from '@/constants';
import { OpeningTypes } from '@/types/OpeningTypes';
import { useAuth } from '@/contexts/AuthProvider';
import { scrollToSection } from '@/utils/InputUtils';
import PageTitle from '@/components/PageTitle';
import { CreateOpeningFileUpload, CreateOpeningForm } from '@/components/CreateOpeningSteps';
import { CreateOpeningFormType } from './definitions';

import { TitleText } from './constants';
import './styles.scss';


const CreateOpening = () => {
  const { selectedClient } = useAuth();
  const [searchParams] = useSearchParams();
  const [currentStep, setCurrentStep] = useState<number>(0);
  const navigate = useNavigate();
  const type = searchParams.get('type');
  const [form, setForm] = useState<CreateOpeningFormType>({ client: selectedClient });
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

    if (form.client && form.client !== selectedClient) {
      setWarnText("District office changed. The form will continue using the originally selected district.")
    };
  }, [type, selectedClient, navigate]);

  const openingType = type! as OpeningTypes;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
  };

  const handleBack = () => setCurrentStep(s => Math.max(0, s - 1));

  const handleNext = () => {
    if (currentStep === 0) {
      if (!form.geojson) {
        setForm((prev) => ({ ...prev, isGeoJsonMissing: true }))
        scrollToSection('opening-map-file-drop-container')
        return;
      }
    }

    setCurrentStep(s => Math.min(s + 1, 2));
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
                ? <CreateOpeningForm isReview={currentStep === 2} form={form} setForm={setForm} />
                : null
            }
          </Grid>
        </Form>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="create-opening-button-grid">
          <Column sm={4} md={4}>
            <Button className="default-button" kind="secondary" onClick={handleBack}>
              Back
            </Button>
          </Column>
          <Column sm={4} md={4}>
            <Button className="default-button" kind="primary" onClick={handleNext} renderIcon={ArrowRight}>
              Next
            </Button>
          </Column>
        </Grid>
      </Column>
    </Grid >
  );
};

export default CreateOpening;
