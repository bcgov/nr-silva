import { useEffect, useState } from 'react';
import { Column, Grid, InlineNotification, ProgressIndicator, ProgressStep } from '@carbon/react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { TENURED_OPENING, GOV_FUNDED_OPENING } from '@/constants';
import PageTitle from '@/components/PageTitle';
import FeatureUnavailable from '@/components/FeatureUnavailable';
import { CreateOpeningRoute, OpeningsRoute } from '@/routes/config';
import { useAuth } from '@/contexts/AuthProvider';
import { hasCreateOpeningPrivilege } from '@/utils/famUtils';
import { CreateOpeningForm } from './CreateOpeningForm';

import './styles.scss';


const CreateOpening = () => {
  const [searchParams] = useSearchParams();
  const auth = useAuth();
  const navigate = useNavigate();
  const type = searchParams.get('type');
  const [currentStep, setCurrentStep] = useState<number>(0);

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

  // Guard: Check authentication and privileges
  if (!auth.user || !hasCreateOpeningPrivilege(auth.user.privileges)) {
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

  // Guard: Check opening type
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

  // Main form: All hooks in CreateOpeningForm are always called
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

      <CreateOpeningForm type={type} currentStep={currentStep} setCurrentStep={setCurrentStep} />
    </Grid>
  );
};

export default CreateOpening;
