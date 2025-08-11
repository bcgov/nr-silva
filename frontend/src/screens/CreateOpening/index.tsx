import { useNavigate, useSearchParams } from 'react-router-dom';
import { TENURED_OPENING, GOV_FUNDED_OPENING } from '@/constants';
import { OpeningTypes } from '@/types/OpeningTypes';
import { Column, Grid, ProgressIndicator, ProgressStep } from '@carbon/react';
import PageTitle from '../../components/PageTitle';
import { TitleText } from './constants';

const CreateOpening = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const type = searchParams.get('type');

  const isValidType =
    type === TENURED_OPENING || type === GOV_FUNDED_OPENING;

  if (!isValidType) {
    console.warn("Invalid opening type");
    navigate("/");
  }

  const openingType = type! as OpeningTypes;


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
          currentIndex={0}
        >
          <ProgressStep
            complete
            description="Step 1: Getting started with Carbon Design System"
            label="First step"
            secondaryLabel="Optional label"
          />
          <ProgressStep
            current
            description="Step 2: Getting started with Carbon Design System"
            label="Second step with tooltip"
          />
          <ProgressStep
            description="Step 3: Getting started with Carbon Design System"
            label="Third step with tooltip"
          />
          <ProgressStep
            description="Step 4: Getting started with Carbon Design System"
            invalid
            label="Fourth step"
            secondaryLabel="Example invalid step"
          />
          <ProgressStep
            description="Step 5: Getting started with Carbon Design System"
            disabled
            label="Fifth step"
          />
        </ProgressIndicator>

      </Column>


    </Grid>
  );
};

export default CreateOpening;
