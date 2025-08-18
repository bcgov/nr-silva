import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { TENURED_OPENING, GOV_FUNDED_OPENING } from '@/constants';
import { OpeningTypes } from '@/types/OpeningTypes';
import { Column, Form, Grid, ProgressIndicator, ProgressStep } from '@carbon/react';
import PageTitle from '../../components/PageTitle';
import { TitleText } from './constants';

import './styles.scss';
import { CreateOpeningFileUpload, CreateOpeningForm } from '@/components/CreateOpeningSteps';
import { useAuth } from '../../contexts/AuthProvider';

const CreateOpening = () => {
  const { selectedClient } = useAuth();
  const [searchParams] = useSearchParams();
  const [currentStep, setCurrentStep] = useState<number>(0);
  const navigate = useNavigate();
  const type = searchParams.get('type');


  const isValidType =
    type === TENURED_OPENING || type === GOV_FUNDED_OPENING;

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
  }, [type, selectedClient, navigate]);



  const openingType = type! as OpeningTypes;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

  };

  const handleBack = () => setCurrentStep(s => Math.max(0, s - 1));


  return (
    <Grid className='default-grid'>
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

      <Column sm={4} md={8} lg={16} xlg={12} max={10}>
        <Form noValidate onSubmit={handleSubmit}>
          <Grid className="create-opening-form-grid">
            {
              currentStep === 0
                ? <CreateOpeningFileUpload />
                : null
            }
            {
              currentStep !== 0
                ? <CreateOpeningForm isReview={currentStep === 2} />
                : null
            }
          </Grid>
        </Form>
      </Column>

    </Grid >
  );
};

export default CreateOpening;
