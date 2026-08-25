import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import CreateOpening from '../../screens/CreateOpening';
import { renderWithProviders } from '../utils/testAuthProvider';
import { GOV_FUNDED_OPENING, TENURED_OPENING } from '../../constants';
import * as famUtils from '../../utils/famUtils';

const mockUseNavigate = vi.fn();
const mockUseSearchParams = vi.fn();
const mockUseAuth = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockUseNavigate,
    useSearchParams: () => mockUseSearchParams(),
  };
});

vi.mock('../../utils/famUtils');
vi.mock('../../utils/featureFlags');

vi.mock('../../contexts/AuthProvider', async () => {
  const actual = await vi.importActual('../../contexts/AuthProvider');
  return {
    ...actual,
    useAuth: () => mockUseAuth(),
  };
});

vi.mock('../../screens/CreateOpening/CreateOpeningForm', () => ({
  CreateOpeningForm: ({ type }: { type: string | null }) => (
    <div data-testid="create-opening-form">Form for type: {type}</div>
  ),
}));

describe('CreateOpening screen', () => {
  beforeEach(() => {
    mockUseNavigate.mockReset();
    mockUseSearchParams.mockReset();
    mockUseAuth.mockReset();
    vi.clearAllMocks();
  });

  it('shows insufficient privileges notification when user lacks privilege', () => {
    vi.mocked(famUtils.hasCreateOpeningPrivilege).mockReturnValue(false);
    mockUseAuth.mockReturnValue({ user: { privileges: {}, associatedClients: [] } });
    mockUseSearchParams.mockReturnValue([new URLSearchParams([['type', TENURED_OPENING]]), vi.fn()]);

    render(<CreateOpening />, { wrapper: renderWithProviders().wrapper });

    expect(screen.getByText('Insufficient privileges')).toBeDefined();
    expect(screen.getByText('You do not have permission to create an opening.')).toBeDefined();
    expect(screen.queryByTestId('create-opening-form')).toBeNull();
  });

  it('shows feature unavailable for government funded opening type', () => {
    vi.mocked(famUtils.hasCreateOpeningPrivilege).mockReturnValue(true);
    mockUseAuth.mockReturnValue({ user: { privileges: {}, associatedClients: [] } });
    mockUseSearchParams.mockReturnValue([new URLSearchParams([['type', GOV_FUNDED_OPENING]]), vi.fn()]);

    render(<CreateOpening />, { wrapper: renderWithProviders().wrapper });

    expect(screen.getByText('Government funded openings are unavailable')).toBeDefined();
    expect(screen.queryByTestId('create-opening-form')).toBeNull();
  });

  it('navigates to / when type is invalid', () => {
    vi.mocked(famUtils.hasCreateOpeningPrivilege).mockReturnValue(true);
    mockUseAuth.mockReturnValue({ user: { privileges: {}, associatedClients: [] } });
    mockUseSearchParams.mockReturnValue([new URLSearchParams([['type', 'invalid']]), vi.fn()]);

    render(<CreateOpening />, { wrapper: renderWithProviders().wrapper });

    expect(mockUseNavigate).toHaveBeenCalledWith('/', { replace: true });
  });

  it('renders CreateOpeningForm when guards pass (tenured opening with privilege)', () => {
    vi.mocked(famUtils.hasCreateOpeningPrivilege).mockReturnValue(true);
    mockUseAuth.mockReturnValue({ user: { privileges: { canCreateOpening: true }, associatedClients: [] } });
    mockUseSearchParams.mockReturnValue([new URLSearchParams([['type', TENURED_OPENING]]), vi.fn()]);

    render(<CreateOpening />, { wrapper: renderWithProviders().wrapper });

    expect(screen.getByTestId('create-opening-form')).toBeDefined();
    expect(screen.getByText(`Form for type: ${TENURED_OPENING}`)).toBeDefined();
    expect(mockUseNavigate).not.toHaveBeenCalledWith('/', expect.anything());
  });
});
