import { SkeletonPlaceholder, SkeletonText, Stack } from '@carbon/react';

import './styles.scss';

/** Loading placeholder that matches one editable tenure item. */
const TenureListItemSkeleton = () => (
  <Stack gap={5} className="tenure-item-skeleton">
    <SkeletonText heading />
    <SkeletonText width="20%" />
    <SkeletonPlaceholder className="tenure-item-skeleton__remove" />
  </Stack>
);

export default TenureListItemSkeleton;
