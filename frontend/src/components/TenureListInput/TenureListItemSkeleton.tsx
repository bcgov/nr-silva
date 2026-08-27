import { Column, Grid, SkeletonPlaceholder, SkeletonText, Stack } from '@carbon/react';

import './styles.scss';

/** Loading placeholder that matches one editable tenure item. */
const TenureListItemSkeleton = () => (
  <Stack gap={6} className="tenure-item-input-container tenure-item-skeleton">
    <SkeletonText heading width="30%" />
    <Grid className="tenure-item-grid">
      {[0, 1, 2].map((index) => (
        <Column key={index} sm={4} md={4} lg={8} className="tenure-column">
          <SkeletonText width="30%" />
          <SkeletonPlaceholder />
        </Column>
      ))}
    </Grid>
    <SkeletonText width="20%" />
    <SkeletonPlaceholder className="tenure-item-skeleton__remove" />
  </Stack>
);

export default TenureListItemSkeleton;
