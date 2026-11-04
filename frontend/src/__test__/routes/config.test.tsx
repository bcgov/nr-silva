import { describe, expect, it } from 'vitest';

import { CreateActivityRoute, EditTenureRoute } from '../../routes/config';
import { CREATE_ACTIVITY_PATH, EDIT_TENURE_PATH } from '../../routes/paths';

describe('EditTenureRoute', () => {
  it('registers the opening-scoped path behind the Edit tenure feature gate', () => {
    expect(EditTenureRoute.path).toBe(EDIT_TENURE_PATH);
    expect(EditTenureRoute.element).toMatchObject({
      props: {
        featureName: 'Edit tenure',
        title: 'Tenure editing is unavailable',
        description: expect.stringContaining('editing workflow is disabled'),
      },
    });
  });
});

describe('CreateActivityRoute', () => {
  it('registers the opening-scoped path behind the Create activity feature gate', () => {
    expect(CreateActivityRoute.path).toBe(CREATE_ACTIVITY_PATH);
    expect(CreateActivityRoute.element).toMatchObject({
      props: {
        featureName: 'Create activity',
        title: 'Activity creation is unavailable',
        description: expect.stringContaining('disabled in this deployment model'),
      },
    });
  });
});
