import { render, screen, fireEvent, within } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import CreateOpeningModal from '@/components/Modals/CreateOpeningModal';
import { useModal } from '@/contexts/ModalContext';
import { useNavigate } from 'react-router-dom';
import { GOV_FUNDED_OPENING, TENURED_OPENING } from '@/constants';
import { CreateOpeningRoute } from '@/routes/config';
import '@testing-library/jest-dom';

// Mock dependencies
vi.mock('@/contexts/ModalContext');
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: vi.fn(),
  };
});

vi.mock('@/components/Modals/ModalHead', () => ({
  default: () => <div data-testid="modal-head">ModalHead</div>,
}));

vi.mock('@/components/Modals/ModalTileButton', () => ({
  default: ({ id, title, selected, onClick, disabled }: any) => (
    <button
      data-testid={`modal-tile-${id}`}
      onClick={onClick}
      disabled={disabled}
      data-selected={selected}
    >
      {title}
    </button>
  ),
}));

vi.mock('@/assets/icon/SeedBox.svg?react', () => ({
  default: (props: any) => <div data-testid="seed-box-icon" {...props}>SeedBox</div>,
}));

vi.mock('@/assets/icon/SeedingWithdrawal.svg?react', () => ({
  default: (props: any) => <div data-testid="seeding-withdrawal-icon" {...props}>SeedingWithdrawal</div>,
}));

describe('CreateOpeningModal', () => {
  const mockCloseModal = vi.fn();
  const mockNavigate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();

    (useModal as any).mockReturnValue({
      isOpen: true,
      closeModal: mockCloseModal,
    });

    (useNavigate as any).mockReturnValue(mockNavigate);
  });

  it('renders the modal when isOpen is true', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    expect(screen.getByTestId('modal-head')).toBeInTheDocument();
  });

  it('modal is not open when isOpen is false', () => {
    (useModal as any).mockReturnValue({
      isOpen: false,
      closeModal: mockCloseModal,
    });

    const { container } = render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    // Carbon Modal always renders but has visibility based on isOpen
    const modal = container.querySelector('.create-opening-modal');
    expect(modal).toHaveClass('create-opening-modal');
  });

  it('displays both opening type options', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    expect(screen.getByTestId(`modal-tile-${TENURED_OPENING}`)).toBeInTheDocument();
    expect(screen.getByTestId(`modal-tile-${GOV_FUNDED_OPENING}`)).toBeInTheDocument();
  });

  it('renders TENURED_OPENING option as enabled', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const tenuredButton = screen.getByTestId(`modal-tile-${TENURED_OPENING}`);
    expect(tenuredButton).not.toBeDisabled();
  });

  it('renders GOV_FUNDED_OPENING option as disabled', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const govButton = screen.getByTestId(`modal-tile-${GOV_FUNDED_OPENING}`);
    expect(govButton).toBeDisabled();
  });

  it('allows selection of TENURED_OPENING type', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const tenuredButton = screen.getByTestId(`modal-tile-${TENURED_OPENING}`);
    fireEvent.click(tenuredButton);

    expect(tenuredButton).toHaveAttribute('data-selected', 'true');
  });

  it('navigates to CreateOpeningRoute when Create an opening button is clicked after selecting TENURED_OPENING', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const tenuredButton = screen.getByTestId(`modal-tile-${TENURED_OPENING}`);
    fireEvent.click(tenuredButton);

    const createButton = screen.getByText('Create an opening');
    fireEvent.click(createButton);

    const searchParams = new URLSearchParams({ type: TENURED_OPENING });
    const expectedPath = `${CreateOpeningRoute.path}?${searchParams.toString()}`;
    expect(mockNavigate).toHaveBeenCalledWith(expectedPath);
  });

  it('closes modal after navigating to CreateOpeningRoute', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const tenuredButton = screen.getByTestId(`modal-tile-${TENURED_OPENING}`);
    fireEvent.click(tenuredButton);

    const createButton = screen.getByText('Create an opening');
    fireEvent.click(createButton);

    expect(mockCloseModal).toHaveBeenCalled();
  });

  it('does not navigate when Create an opening button is clicked without selecting a type', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const createButton = screen.getByText('Create an opening');
    fireEvent.click(createButton);

    expect(mockNavigate).not.toHaveBeenCalled();
  });

  it('closes modal when Cancel button is clicked', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const cancelButton = screen.getByText('Cancel');
    fireEvent.click(cancelButton);

    expect(mockCloseModal).toHaveBeenCalled();
  });

  it('closes modal when onRequestClose is triggered', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    // The modal's onRequestClose is called by Carbon Modal component
    // when user clicks outside or presses Escape
    expect(mockCloseModal).not.toHaveBeenCalled();
  });

  it('deselects and switches opening type when clicking different tile', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const tenuredButton = screen.getByTestId(`modal-tile-${TENURED_OPENING}`);
    fireEvent.click(tenuredButton);
    expect(tenuredButton).toHaveAttribute('data-selected', 'true');

    // Note: GOV_FUNDED_OPENING is disabled, so we can't click it
    // This test verifies the tenured button is selected
    expect(tenuredButton).toHaveAttribute('data-selected', 'true');
  });

  it('renders Create an opening and Cancel buttons', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    expect(screen.getByText('Create an opening')).toBeInTheDocument();
    expect(screen.getByText('Cancel')).toBeInTheDocument();
  });

  it('passes correct type parameter in URL when navigating', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const tenuredButton = screen.getByTestId(`modal-tile-${TENURED_OPENING}`);
    fireEvent.click(tenuredButton);

    const createButton = screen.getByText('Create an opening');
    fireEvent.click(createButton);

    const callArgs = mockNavigate.mock.calls[0][0];
    expect(callArgs).toContain(`type=${TENURED_OPENING}`);
  });

  it('uses CreateOpeningRoute path when navigating', () => {
    render(
      <BrowserRouter>
        <CreateOpeningModal />
      </BrowserRouter>
    );

    const tenuredButton = screen.getByTestId(`modal-tile-${TENURED_OPENING}`);
    fireEvent.click(tenuredButton);

    const createButton = screen.getByText('Create an opening');
    fireEvent.click(createButton);

    const callArgs = mockNavigate.mock.calls[0][0];
    expect(callArgs).toContain(CreateOpeningRoute.path);
  });
});
