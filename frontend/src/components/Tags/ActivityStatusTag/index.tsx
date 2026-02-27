import { Tag } from '@carbon/react';

type ActivityStatusTagProps = {
  isComplete: boolean;
};

const ActivityStatusTag = ({ isComplete }: ActivityStatusTagProps) => (
  <Tag
    className="default-dropdown-tag"
    title={isComplete ? 'Complete' : 'Planned'}
    type={isComplete ? 'purple' : 'blue'}
  >
    {isComplete ? 'Complete' : 'Planned'}
  </Tag>
);

export default ActivityStatusTag;
