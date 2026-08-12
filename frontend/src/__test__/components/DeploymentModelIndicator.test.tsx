import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import DeploymentModelIndicator from '../../components/DeploymentModelIndicator';
import * as envModule from '@/env';
import * as featureFlagsModule from '../../utils/featureFlags';

vi.mock('@/env', () => ({
  env: {
    VITE_ZONE: 'TEST',
    VITE_DEPLOYMENT_MODEL: 'hybrid'
  }
}));

vi.mock('../../utils/featureFlags', () => ({
  isNonProduction: vi.fn(() => true)
}));

describe('DeploymentModelIndicator', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    (featureFlagsModule.isNonProduction as any).mockReturnValue(true);
  });

  it('should render the indicator in non-production environments', () => {
    (envModule.env as any).VITE_ZONE = 'TEST';
    (envModule.env as any).VITE_DEPLOYMENT_MODEL = 'hybrid';

    render(<DeploymentModelIndicator />);

    expect(screen.getByText('Hybrid Model')).toBeTruthy();
  });

  it('should display "Postgres Model" when postgres is configured', () => {
    (envModule.env as any).VITE_ZONE = 'DEV';
    (envModule.env as any).VITE_DEPLOYMENT_MODEL = 'postgres';

    render(<DeploymentModelIndicator />);

    expect(screen.getByText('Postgres Model')).toBeTruthy();
  });

  it('should not render in production', () => {
    (featureFlagsModule.isNonProduction as any).mockReturnValue(false);

    const { container } = render(<DeploymentModelIndicator />);

    expect(container.firstChild).toBeNull();
  });

  it('should display "Hybrid Model" by default', () => {
    (envModule.env as any).VITE_ZONE = 'TEST';
    (envModule.env as any).VITE_DEPLOYMENT_MODEL = undefined;

    render(<DeploymentModelIndicator />);

    expect(screen.getByText('Hybrid Model')).toBeTruthy();
  });
});
