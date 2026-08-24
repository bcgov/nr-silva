import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import CreateOpeningSuccess from '../../../screens/CreateOpening/CreateOpeningSuccess';

const mockNavigate = vi.fn();
const mockSearchParams = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useSearchParams: () => mockSearchParams(),
  };
});

describe('CreateOpeningSuccess', () => {
  beforeEach(() => {
    mockNavigate.mockReset();
    mockSearchParams.mockReset();
  });

  it('renders success message when openingId is present', () => {
    mockSearchParams.mockReturnValue([new URLSearchParams([['openingId', '123']]), vi.fn()]);

    render(<CreateOpeningSuccess />);

    expect(screen.getByText('Success')).toBeDefined();
    expect(screen.getByText('New opening created')).toBeDefined();
    expect(screen.getByText(/Opening ID:/)).toBeDefined();
    expect(screen.getByText('123')).toBeDefined();

    const button = screen.getByRole('button', { name: /View opening/i });
    fireEvent.click(button);
    expect(mockNavigate).toHaveBeenCalledWith('/openings/123');
  });

  it('renders empty section when openingId is missing', () => {
    mockSearchParams.mockReturnValue([new URLSearchParams(), vi.fn()]);

    render(<CreateOpeningSuccess />);

    expect(screen.getByText('Opening ID not found')).toBeDefined();
    expect(screen.getByText('Unable to display creation confirmation.')).toBeDefined();
  });
});
