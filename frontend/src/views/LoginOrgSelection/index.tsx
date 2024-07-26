import React from 'react';
import { FlexGrid, Row, Column } from '@carbon/react';

import OrganizationSelection from '../../components/OrganizationSelection';

import './styles.scss';
import { RootState } from '../../store';
import { useSelector } from 'react-redux';

const LoginOrgSelection = () => {
  const userDetails = useSelector((state: RootState) => state.userDetails);
  const user = userDetails.user;

  return (
    <FlexGrid className="login-org-selection-grid">
      <Row className="row-container">
        <Column className="col-container" sm={4} md={6} lg={14} xlg={10} max={8}>
          <section className="title-section">
            <h2 className="title-text">
              Organization selection
            </h2>
            <p className="subtitle-text">
              {`${user?.firstName} ${user?.lastName} (${user?.providerUsername}) select which organization you're representing.`}
            </p>
          </section>
          <OrganizationSelection />
        </Column>
      </Row>
    </FlexGrid>
  );
};

export default LoginOrgSelection;
