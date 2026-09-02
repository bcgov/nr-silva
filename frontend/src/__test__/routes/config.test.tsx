import { describe, expect, it } from 'vitest';

import { EditTenureRoute } from '@/routes/config';
import { EDIT_TENURE_PATH } from '@/routes/paths';

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
