import type { ReactNode } from 'react';
import { Button } from '@carbon/react';
import { useNavigate } from 'react-router-dom';
import EmptySection from '../EmptySection';
import { DashboardRoute } from '@/routes/config';
import './styles.scss';

interface FeatureUnavailableProps {
  featureName: string;
  title?: string;
  description?: string | ReactNode;
  actionLabel?: string;
  onActionClick?: () => void;
  className?: string;
}

const FeatureUnavailable = ({
  featureName,
  title,
  description,
  actionLabel = 'Back to dashboard',
  onActionClick,
  className,
}: FeatureUnavailableProps) => {
  const navigate = useNavigate();
  const handleClick = onActionClick ?? (() => navigate(DashboardRoute.path!));

  return (
    <div className={`feature-unavailable-container ${className ?? ''}`}>
      <EmptySection
        icon="Construction"
        title={title ?? `${featureName} is not available`}
        description={description ?? `This feature is not currently available.`}
      />
      <Button kind="primary" onClick={handleClick} className="feature-unavailable-button">
        {actionLabel}
      </Button>
    </div>
  );
};

export default FeatureUnavailable;
