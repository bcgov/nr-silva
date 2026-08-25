import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, vi } from 'vitest';
import FeatureGateRoute from '../../routes/FeatureGateRoute';
import * as featureFlags from '../../utils/featureFlags';

vi.mock('../../utils/featureFlags');

const MockChild = () => <div>Child route content</div>;

describe('FeatureGateRoute', () => {
  it('renders children when gate is enabled', () => {
    vi.mocked(featureFlags.gatePostgresFeature).mockReturnValue(false);

    render(
      <MemoryRouter initialEntries={['/test']}>
        <Routes>
          <Route path="/test" element={<FeatureGateRoute featureName="Test" title="Blocked" description="Blocked" actionLabel="Back"> <MockChild /> </FeatureGateRoute>} />
        </Routes>
      </MemoryRouter>
    );

    expect(screen.getByText('Child route content')).toBeDefined();
  });

  it('renders FeatureUnavailable when gate is disabled', () => {
    vi.mocked(featureFlags.gatePostgresFeature).mockReturnValue(true);
    render(
      <MemoryRouter initialEntries={['/test']}>
        <Routes>
          <Route path="/test" element={<FeatureGateRoute featureName="Test" title="Blocked" description="Blocked" actionLabel="Back" > <MockChild /> </FeatureGateRoute>} />
        </Routes>
      </MemoryRouter>
    );

    expect(screen.getByText('Blocked')).toBeDefined();
    expect(screen.queryByText('Child route content')).toBeNull();
  });
});
