import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Column, Grid, Modal, RadioButton, Stack } from '@carbon/react';
import PageTitle from '@/components/PageTitle';
import { ActivitySearchRoute } from '@/routes/config';
import ActivitiesSearchSection from '@/components/ActivitySearchSection';
import { hasActivitySearchFilters, readActivitySearchUrlParams } from '@/components/ActivitySearchSection/utils';
import DisturbancesSearchSection from '@/components/DisturbancesSearchSection';

import './styles.scss';

type props = {
  type: 'activities' | 'disturbances';
}

const ActivitySearch = ({ type }: props) => {
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);

  useEffect(() => {
    document.title = `Activities Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  const navigate = useNavigate();
  if (!type) {
    navigate('/');
    return null;
  }

  const handleSearchTypeChange = (value: props['type']) => {
    if (value === type) return;
    if (type === 'activities' && hasActivitySearchFilters(readActivitySearchUrlParams())) {
      setIsConfirmModalOpen(true);
      return;
    }

    navigate(`${ActivitySearchRoute.path}/${value}`);
  };

  const handleConfirmSwitch = () => {
    const next = type === 'activities' ? 'disturbances' : 'activities';
    navigate(`${ActivitySearchRoute.path}/${next}`);
    setIsConfirmModalOpen(false);
  };

  const handleCancelSwitch = () => {
    setIsConfirmModalOpen(false);
  };

  return (
    <Grid className="default-grid default-search-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Activities" />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Stack gap={3}>
          <label className="default-label" htmlFor="activities-search-radio-button">Search for</label>
          <div className="default-horizontal-radio-group">
            <RadioButton
              id="activities-search-radio-button"
              labelText="Activities"
              value="activities"
              checked={type === 'activities'}
              onChange={() => handleSearchTypeChange('activities')}
            />
            <RadioButton
              id="disturbances-search-radio-button"
              labelText="Disturbances - coming soon"
              value="disturbances"
              checked={type === 'disturbances'}
              onChange={() => handleSearchTypeChange('disturbances')}
              disabled
            />
          </div>
        </Stack>
        <hr className="default-hr-for-radio-btn-tab" />
      </Column>

      <Column className="full-width-col" sm={4} md={8} lg={16}>
        {
          type === 'activities'
            ? (
              <ActivitiesSearchSection />
            ) : (
              <DisturbancesSearchSection />
            )
        }
      </Column>
      <Modal
        open={isConfirmModalOpen}
        className="default-confirm-switch-modal"
        modalHeading="Switch search type?"
        primaryButtonText="Proceed"
        secondaryButtonText="Cancel"
        onRequestSubmit={handleConfirmSwitch}
        onRequestClose={handleCancelSwitch}
        onSecondarySubmit={handleCancelSwitch}
        danger
        size="sm"
      >
        <p>
          Switching to a different search type will clear your current search inputs and results.
          Are you sure you want to continue?
        </p>
      </Modal>
    </Grid>
  );
};

export default ActivitySearch;
