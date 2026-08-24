import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import CreateOpening from '../../screens/CreateOpening';
import { renderWithProviders } from '../utils/testAuthProvider';
import { GOV_FUNDED_OPENING } from '../../constants';

const mockUseNavigate = vi.fn();
const mockUseSearchParams = vi.fn();
const mockHasCreateOpeningPrivilege = vi.fn();
const mockUseAuth = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockUseNavigate,
    useSearchParams: () => mockUseSearchParams(),
  };
});

vi.mock('../../utils/famUtils', async () => {
  const actual = await vi.importActual('../../utils/famUtils');
  return {
    ...actual,
    hasCreateOpeningPriviledge: () => mockHasCreateOpeningPrivilege(),
  };
});

vi.mock('../../contexts/AuthProvider', async () => {
  const actual = await vi.importActual('../../contexts/AuthProvider');
  return {
    ...actual,
    useAuth: () => mockUseAuth(),
  };
});

describe('CreateOpening screen', () => {
  beforeEach(() => {
    mockUseNavigate.mockReset();
    mockUseSearchParams.mockReset();
    mockHasCreateOpeningPrivilege.mockReset();
    mockUseAuth.mockReset();
  });

  it('shows insufficient privileges notification when user lacks privilege', () => {
    mockUseAuth.mockReturnValue({ user: { privileges: {}, associatedClients: [] } });
    mockHasCreateOpeningPrivilege.mockReturnValue(false);
    mockUseSearchParams.mockReturnValue([new URLSearchParams([['type', 'tenured']]), vi.fn()]);

    render(<CreateOpening />, { wrapper: renderWithProviders().wrapper });

    expect(screen.getByText('Insufficient privileges')).toBeDefined();
    expect(screen.getByText('You do not have permission to create an opening.')).toBeDefined();
  });

  it('shows feature unavailable for government funded opening type', () => {
    mockUseAuth.mockReturnValue({ user: { privileges: {}, associatedClients: [] } });
    mockHasCreateOpeningPrivilege.mockReturnValue(true);
    mockUseSearchParams.mockReturnValue([new URLSearchParams([['type', GOV_FUNDED_OPENING]]), vi.fn()]);

    render(<CreateOpening />, { wrapper: renderWithProviders().wrapper });

    expect(screen.getByText('Government funded openings are unavailable')).toBeDefined();
  });

  it('navigates to / when type is invalid', () => {
    mockUseAuth.mockReturnValue({ user: { privileges: {}, associatedClients: [] } });
    mockHasCreateOpeningPrivilege.mockReturnValue(true);
    mockUseSearchParams.mockReturnValue([new URLSearchParams([['type', 'invalid']]), vi.fn()]);

    render(<CreateOpening />, { wrapper: renderWithProviders().wrapper });
    expect(mockUseNavigate).toHaveBeenCalledWith('/', { replace: true });
  });
});
