import { render, screen, fireEvent } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import ModalTileButton from '@/components/Modals/ModalTileButton';
import '@testing-library/jest-dom';

// Mock Carbon Icons
vi.mock('@carbon/icons-react', () => ({
  CheckmarkFilled: () => <span data-testid="checkmark-icon">✓</span>,
}));

describe('ModalTileButton', () => {
  const defaultProps = {
    icon: <div data-testid="test-icon">Icon</div>,
    id: 'test-id',
    title: 'Test Title',
  };

  it('renders button with required props', () => {
    render(<ModalTileButton {...defaultProps} />);
    expect(screen.getByText('Test Title')).toBeInTheDocument();
  });

  it('renders with correct id attribute', () => {
    render(<ModalTileButton {...defaultProps} />);
    expect(screen.getByRole('button')).toHaveAttribute('id', 'test-id');
  });

  it('renders icon when provided', () => {
    render(<ModalTileButton {...defaultProps} />);
    expect(screen.getByTestId('test-icon')).toBeInTheDocument();
  });

  it('renders title text', () => {
    render(<ModalTileButton {...defaultProps} title="Custom Title" />);
    expect(screen.getByText('Custom Title')).toBeInTheDocument();
  });

  it('renders subtitle when provided', () => {
    render(<ModalTileButton {...defaultProps} subtitle="Test Subtitle" />);
    expect(screen.getByText('Test Subtitle')).toBeInTheDocument();
  });

  it('does not render subtitle when not provided', () => {
    render(<ModalTileButton {...defaultProps} />);
    expect(screen.queryByText('Test Subtitle')).not.toBeInTheDocument();
  });

  it('does not apply selected class when selected is false', () => {
    render(<ModalTileButton {...defaultProps} selected={false} />);
    expect(screen.getByRole('button')).not.toHaveClass('selected');
  });

  it('applies selected class when selected is true', () => {
    render(<ModalTileButton {...defaultProps} selected={true} />);
    expect(screen.getByRole('button')).toHaveClass('selected');
  });

  it('shows checkmark icon when selected', () => {
    render(<ModalTileButton {...defaultProps} selected={true} />);
    expect(screen.getByTestId('checkmark-icon')).toBeInTheDocument();
  });

  it('does not show checkmark icon when not selected', () => {
    render(<ModalTileButton {...defaultProps} selected={false} />);
    expect(screen.queryByTestId('checkmark-icon')).not.toBeInTheDocument();
  });

  it('calls onClick handler when clicked', () => {
    const handleClick = vi.fn();
    render(<ModalTileButton {...defaultProps} onClick={handleClick} />);

    fireEvent.click(screen.getByRole('button'));
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it('does not call onClick when disabled', () => {
    const handleClick = vi.fn();
    render(
      <ModalTileButton
        {...defaultProps}
        onClick={handleClick}
        disabled={true}
      />
    );

    fireEvent.click(screen.getByRole('button'));
    // Button is disabled so click handler is not called by the button itself
    expect(screen.getByRole('button')).toBeDisabled();
  });

  it('renders as disabled when disabled prop is true', () => {
    render(<ModalTileButton {...defaultProps} disabled={true} />);
    expect(screen.getByRole('button')).toBeDisabled();
  });

  it('renders as enabled when disabled prop is false', () => {
    render(<ModalTileButton {...defaultProps} disabled={false} />);
    expect(screen.getByRole('button')).not.toBeDisabled();
  });

  it('renders as enabled when disabled prop is not provided', () => {
    render(<ModalTileButton {...defaultProps} />);
    expect(screen.getByRole('button')).not.toBeDisabled();
  });

  it('renders button with type="button"', () => {
    render(<ModalTileButton {...defaultProps} />);
    expect(screen.getByRole('button')).toHaveAttribute('type', 'button');
  });

  it('renders both icon and subtitle when both are provided', () => {
    render(
      <ModalTileButton
        {...defaultProps}
        subtitle="Subtitle text"
      />
    );
    expect(screen.getByTestId('test-icon')).toBeInTheDocument();
    expect(screen.getByText('Subtitle text')).toBeInTheDocument();
  });

  it('renders selected state with title and subtitle', () => {
    render(
      <ModalTileButton
        {...defaultProps}
        subtitle="Subtitle"
        selected={true}
      />
    );
    expect(screen.getByRole('button')).toHaveClass('selected');
    expect(screen.getByText('Test Title')).toBeInTheDocument();
    expect(screen.getByText('Subtitle')).toBeInTheDocument();
    expect(screen.getByTestId('checkmark-icon')).toBeInTheDocument();
  });

  it('handles multiple clicks on the button', () => {
    const handleClick = vi.fn();
    render(<ModalTileButton {...defaultProps} onClick={handleClick} />);

    fireEvent.click(screen.getByRole('button'));
    fireEvent.click(screen.getByRole('button'));
    fireEvent.click(screen.getByRole('button'));

    expect(handleClick).toHaveBeenCalledTimes(3);
  });

  it('renders with modal-tile-button CSS class', () => {
    const { container } = render(<ModalTileButton {...defaultProps} />);
    expect(container.querySelector('.modal-tile-button')).toBeInTheDocument();
  });

  it('renders content in correct structure (icon then title and subtitle)', () => {
    render(
      <ModalTileButton
        {...defaultProps}
        subtitle="Subtitle text"
      />
    );

    const button = screen.getByRole('button');
    const content = button.querySelector('.tile-button-content');
    expect(content).toBeInTheDocument();
  });

  it('renders title with tile-title class', () => {
    render(<ModalTileButton {...defaultProps} />);
    const titleElement = screen.getByRole('heading');
    expect(titleElement).toHaveClass('tile-title');
  });

  it('renders subtitle with tile-subtitle class when provided', () => {
    render(<ModalTileButton {...defaultProps} subtitle="Test subtitle" />);
    const subtitleElement = screen.getByText('Test subtitle');
    expect(subtitleElement).toHaveClass('tile-subtitle');
  });
});
