import type { ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import FeatureUnavailable from '@/components/FeatureUnavailable';
import { OPENINGS_PATH } from '@/routes/paths';
import { gatePostgresFeature } from '@/utils/featureFlags';

interface FeatureGateRouteProps {
  featureName: string;
  title: string;
  description: string;
  actionLabel?: string;
  children: ReactNode;
}

const FeatureGateRoute = ({
  featureName,
  title,
  description,
  actionLabel,
  children,
}: FeatureGateRouteProps) => {
  const navigate = useNavigate();

  if (gatePostgresFeature()) {
    return (
      <FeatureUnavailable
        featureName={featureName}
        title={title}
        description={description}
        actionLabel={actionLabel ?? 'Back to openings'}
        onActionClick={() => navigate(OPENINGS_PATH)}
      />
    );
  }

  return <>{children}</>;
};

export default FeatureGateRoute;
