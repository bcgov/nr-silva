import { env } from '@/env';
import { getDeploymentModel, DEPLOYMENT_MODELS } from '@/constants/deploymentModel';

/**
 * Check if we're in a non-production environment
 */
export const isNonProduction = (): boolean => {
  const zone = env.VITE_ZONE?.toLowerCase();
  return zone !== 'prod';
};

/**
 * Check if oracle-only features should be available (not gated)
 * Features are gated (blocked) when in non-prod postgres deployments
 * Features are available when in production OR using hybrid/oracle deployment
 */
export const gatePostgresFeature = (): boolean => {
  return !isNonProduction() || getDeploymentModel(env.VITE_DEPLOYMENT_MODEL) === DEPLOYMENT_MODELS.HYBRID;
};
