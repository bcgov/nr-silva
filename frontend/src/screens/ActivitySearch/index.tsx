import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Column, Grid, RadioButton, Stack } from '@carbon/react';
import PageTitle from '@/components/PageTitle';
import { ActivitySearchRoute } from '@/routes/config';
import ActivitiesSearchSection from './ActivitiesSearchSection';
import DisturbancesSearchSection from './DisturbancesSearchSection';

import './styles.scss';

type props = {
  type: 'activities' | 'disturbances';
}

const ActivitySearch = ({ type }: props) => {
  const navigate = useNavigate();
  if (!type) {
    navigate('/');
    return null;
  }

  const handleSearchTypeChange = (value: props['type']) => {
    if (value === type) return;
    navigate(`${ActivitySearchRoute.path}/${value}`);
  }

  useEffect(() => {
    document.title = `Activities Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

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
              labelText="Disturbances"
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
    </Grid>
  );
};

export default ActivitySearch;
