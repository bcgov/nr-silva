import React from 'react';
import { FlexGrid, Column } from '@carbon/react';
import { useAuth } from '@/contexts/AuthProvider';
import DistrictSelection from '@/components/DistrictSelection';

import './styles.scss';

const LoginClientSelection = () => {
  const { user } = useAuth();

  return (
    <FlexGrid className="login-org-selection-grid">
      <Column className="col-container" sm={4} md={6} lg={14} xlg={10} max={8}>
        <section className="title-section">
          <h2 className="title-text">
            District office selection
          </h2>
          <p className="subtitle-text">
            {`${user?.firstName} ${user?.lastName} (${user?.providerUsername}) select which district office you're representing.`}
          </p>
        </section>
        <DistrictSelection />
      </Column>
    </FlexGrid>
  );
};

export default LoginClientSelection;
