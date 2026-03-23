import React from 'react';
import { env } from '@/env';
import {
  getDeploymentModel,
  getDeploymentModelDisplayName
} from '@/constants/deploymentModel';
import { Tag } from '@carbon/react';

import "./styles.scss";

/**
 * Debug component that displays the current deployment model
 * Only visible in non-production environments (dev, test, PR deployments)
 *
 * This component should be integrated into the app layout for debugging purposes.
 */
const DeploymentModelIndicator: React.FC = () => {
  // Determine if we should show the indicator
  const zone = env.VITE_ZONE?.toLowerCase();
  const isProd = zone === 'prod';

  // Don't render in production
  if (isProd) {
    return null;
  }

  const deploymentModel = getDeploymentModel(env.VITE_DEPLOYMENT_MODEL);
  const displayName = getDeploymentModelDisplayName(deploymentModel);

  return (
    <div className="indicator">
      <span className="label">DB:</span>
      <Tag type={deploymentModel === 'postgres' ? 'green' : 'blue'}>
        {displayName}
      </Tag>
    </div>
  );
};

export default DeploymentModelIndicator;
