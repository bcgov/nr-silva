import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { Button, Column, Grid, InlineNotification, Stack } from '@carbon/react';
import { ArrowRight, Construction, Document } from '@carbon/icons-react';
import PageTitle from '@/components/PageTitle';
import { CreateOpeningRoute, OpeningsRoute } from '@/routes/config';
import EmptySection from '@/components/EmptySection';

const CreateOpeningSuccess = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const openingId = searchParams.get('openingId');

  useEffect(() => {
    document.title = 'Opening Created - Silva';
    return () => {
      document.title = 'Silva';
    };
  }, []);

  const handleViewOpening = () => {
    if (openingId) {
      navigate(`/openings/${openingId}`);
    }
  };

  if (!openingId) {
    return <EmptySection icon="BreakingChange" title="Opening ID not found" description="Unable to display creation confirmation." />;
  }

  return (
    <div className='default-centred-container'>
      <Stack gap={6} className='default-centred-container-stack'>
        <InlineNotification
          kind="success"
          title="Success"
          subtitle="New opening created"
          lowContrast
          hideCloseButton
        />
        <div>
          <h1>Opening ID:{' '}<strong>{openingId}</strong></h1>
        </div>
        <Grid className='default-button-grid'>
          <Column sm={4} md={4} lg={6} max={4} className='default-button-grid-column'>
            <Button kind="tertiary" onClick={handleViewOpening}>View opening</Button>
          </Column>

          <Column sm={4} md={4} lg={6} max={4} className='default-button-grid-column'>
            <Button kind='primary' disabled renderIcon={Construction}>Add standards units</Button>
          </Column>
        </Grid>
      </Stack>
    </div>
  );
};

export default CreateOpeningSuccess;
