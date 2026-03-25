/**
 * Deployment model configuration for database backend support
 * Determines which database the frontend connects to:
 * - 'hybrid': Oracle database (default, used in production)
 * - 'postgres': PostgreSQL database
 */

export const DEPLOYMENT_MODELS = {
  HYBRID: 'hybrid',
  POSTGRES: 'postgres'
} as const;

export type DeploymentModel = (typeof DEPLOYMENT_MODELS)[keyof typeof DEPLOYMENT_MODELS];

/**
 * Get the current deployment model from environment
 * Defaults to 'hybrid' if not specified
 */
export const getDeploymentModel = (envValue?: string): DeploymentModel => {
  const value = envValue?.toLowerCase().trim();
  if (value === DEPLOYMENT_MODELS.POSTGRES) {
    return DEPLOYMENT_MODELS.POSTGRES;
  }
  return DEPLOYMENT_MODELS.HYBRID;
};

/**
 * Display name for deployment model
 */
export const getDeploymentModelDisplayName = (model: DeploymentModel): string => {
  if (model === DEPLOYMENT_MODELS.POSTGRES) {
    return 'Postgres Model';
  }
  return 'Hybrid Model';
};
