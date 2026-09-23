import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import * as featureFlags from '@/utils/featureFlags';
import Openings from '@/screens/Openings';

const mockNavigate = vi.fn();
const mockOpenModal = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

vi.mock('@/contexts/ModalContext', () => ({
  useModal: () => ({ openModal: mockOpenModal }),
}));

vi.mock('@/utils/featureFlags', () => ({
  gatePostgresFeature: vi.fn(),
}));

vi.mock('@/components/RecentOpenings', () => ({
  default: () => <div data-testid="recent-openings">Recent Openings Mock</div>,
}));

vi.mock('@/components/MyOpenings', () => ({
  default: () => <div data-testid="my-openings">My Openings Mock</div>,
}));

describe('Openings Screen', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(featureFlags.gatePostgresFeature).mockReturnValue(false);
  });

  const renderComponent = (initialEntries = ['/openings']) => {
    return render(
      <MemoryRouter initialEntries={initialEntries}>
        <Openings />
      </MemoryRouter>
    );
  };

  it('updates document title on mount and resets on unmount', () => {
    const { unmount } = renderComponent();
    expect(document.title).toBe('Openings - Silva');

    unmount();
    expect(document.title).toBe('Silva');
  });

  it('renders page title and tab list', async () => {
    renderComponent();

    expect(screen.getByText('Openings')).toBeInTheDocument();
    expect(screen.getByText('Recent openings')).toBeInTheDocument();
    expect(screen.getByText('My openings')).toBeInTheDocument();

    const recentTabContent = await screen.findByTestId('recent-openings');
    expect(recentTabContent).toBeInTheDocument();
  });

  it('renders Create new button when feature is not gated and calls openModal', () => {
    vi.mocked(featureFlags.gatePostgresFeature).mockReturnValue(false);
    renderComponent();

    const createBtn = screen.getByRole('button', { name: /Create new/i });
    expect(createBtn).toBeInTheDocument();

    fireEvent.click(createBtn);
    expect(mockOpenModal).toHaveBeenCalledWith('CREATE_OPENING');
  });

  it('does not render Create new button when feature is gated', () => {
    vi.mocked(featureFlags.gatePostgresFeature).mockReturnValue(true);
    renderComponent();

    expect(screen.queryByRole('button', { name: /Create new/i })).toBeNull();
  });

  it('handles opening ID input, sanitizes input, and navigates on Go button or Enter key', () => {
    renderComponent();

    const input = screen.getByPlaceholderText('View Opening by ID');

    // Typing non-digits are filtered by sanitizeDigits
    fireEvent.change(input, { target: { value: 'abc12345xyz' } });
    expect(input).toHaveValue('12345');

    // Click Go button
    const goBtn = screen.getByRole('button', { name: 'Navigate to opening' });
    fireEvent.click(goBtn);
    expect(mockNavigate).toHaveBeenCalledWith('/openings/12345');

    // Press Enter
    fireEvent.keyDown(input, { key: 'Enter' });
    expect(mockNavigate).toHaveBeenCalledWith('/openings/12345');
  });

  it('handles paste event into opening ID input', () => {
    renderComponent();

    const input = screen.getByPlaceholderText('View Opening by ID');

    fireEvent.paste(input, {
      clipboardData: {
        getData: () => 'abc998877def',
      },
    });

    expect(input).toHaveValue('998877');
  });

  it('does not navigate if opening ID input is empty', () => {
    renderComponent();

    const goBtn = screen.getByRole('button', { name: 'Navigate to opening' });
    fireEvent.click(goBtn);

    expect(mockNavigate).not.toHaveBeenCalled();
  });

  it('switches tabs and loads MyOpenings', async () => {
    renderComponent(['/openings?tab=my-openings']);

    const myTabContent = await screen.findByTestId('my-openings');
    expect(myTabContent).toBeInTheDocument();
  });

  it('changes active tab when tab is clicked', async () => {
    renderComponent();

    expect(screen.getByTestId('recent-openings')).toBeInTheDocument();
    expect(screen.queryByTestId('my-openings')).toBeNull();

    const myOpeningsTab = screen.getByRole('tab', { name: 'My openings' });
    fireEvent.click(myOpeningsTab);

    expect(await screen.findByTestId('my-openings')).toBeInTheDocument();
    expect(screen.queryByTestId('recent-openings')).toBeNull();
  });
});
