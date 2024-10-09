import React, { useEffect } from 'react';
import { FlexGrid, Row, Column } from '@carbon/react';
import OrganizationSelection from '../../components/OrganizationSelection';
import { RootState } from '../../store';
import { useSelector } from 'react-redux';
import './styles.scss';

/**
 * Renders the organization selection view after login.
 * 
 * This view allows users to select which organization they are representing.
 * It displays the user's name and username, prompting them to choose an organization.
 * 
 * @returns {JSX.Element} The rendered LoginOrgSelection component.
 */
function LoginOrgSelection (): JSX.Element {
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
              {`${user?.firstName} ${user?.lastName} ${user.providerUsername ? "("+user.providerUsername+")": ""} select which organization you're representing.`}
            </p>
          </section>
          <OrganizationSelection />
        </Column>
      </Row>
    </FlexGrid>
  );
};

export default LoginOrgSelection;
