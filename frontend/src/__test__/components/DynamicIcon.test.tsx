import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import DynamicIcon from '@/components/DynamicIcon';
import '@testing-library/jest-dom';

// Mock Carbon icons - use Proxy to handle dynamic access
vi.mock('@carbon/icons-react', () => {
  const mockIcons: Record<string, any> = {
    CheckmarkFilled: ({ size }: any) => (
      <svg data-testid="checkmark-icon" data-size={size} />
    ),
    CloseFilled: ({ size }: any) => (
      <svg data-testid="close-icon" data-size={size} />
    ),
    AddFilled: ({ size }: any) => (
      <svg data-testid="add-icon" data-size={size} />
    ),
    Error: ({ size }: any) => (
      <svg data-testid="error-icon" data-size={size} />
    ),
  };

  return new Proxy(mockIcons, {
    get: (target, prop) => {
      return target[prop as string];
    },
  });
});

describe('DynamicIcon', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders icon component by name', () => {
    render(<DynamicIcon iconName="CheckmarkFilled" />);
    expect(screen.getByTestId('checkmark-icon')).toBeInTheDocument();
  });

  it('renders different icon by iconName', () => {
    const { rerender } = render(<DynamicIcon iconName="CheckmarkFilled" />);
    expect(screen.getByTestId('checkmark-icon')).toBeInTheDocument();

    rerender(<DynamicIcon iconName="CloseFilled" />);
    expect(screen.getByTestId('close-icon')).toBeInTheDocument();
  });

  it('applies default size of 24', () => {
    render(<DynamicIcon iconName="CheckmarkFilled" />);
    const icon = screen.getByTestId('checkmark-icon');
    expect(icon).toHaveAttribute('data-size', '24');
  });

  it('applies custom size prop', () => {
    render(<DynamicIcon iconName="CheckmarkFilled" size={32} />);
    const icon = screen.getByTestId('checkmark-icon');
    expect(icon).toHaveAttribute('data-size', '32');
  });

  it('renders with size 16', () => {
    render(<DynamicIcon iconName="CheckmarkFilled" size={16} />);
    const icon = screen.getByTestId('checkmark-icon');
    expect(icon).toHaveAttribute('data-size', '16');
  });

  it('renders with size 20', () => {
    render(<DynamicIcon iconName="CheckmarkFilled" size={20} />);
    const icon = screen.getByTestId('checkmark-icon');
    expect(icon).toHaveAttribute('data-size', '20');
  });

  it('renders AddFilled icon', () => {
    render(<DynamicIcon iconName="AddFilled" />);
    expect(screen.getByTestId('add-icon')).toBeInTheDocument();
  });

  it('renders Error icon', () => {
    render(<DynamicIcon iconName="Error" />);
    expect(screen.getByTestId('error-icon')).toBeInTheDocument();
  });

  it('renders icon with size 48', () => {
    render(<DynamicIcon iconName="CloseFilled" size={48} />);
    const icon = screen.getByTestId('close-icon');
    expect(icon).toHaveAttribute('data-size', '48');
  });

  it('renders multiple icons with different sizes', () => {
    const { container } = render(
      <>
        <DynamicIcon iconName="CheckmarkFilled" size={16} />
        <DynamicIcon iconName="CloseFilled" size={32} />
      </>
    );
    const icons = container.querySelectorAll('svg');
    expect(icons.length).toBe(2);
  });

  it('updates icon when iconName changes', () => {
    const { rerender } = render(<DynamicIcon iconName="CheckmarkFilled" />);
    expect(screen.getByTestId('checkmark-icon')).toBeInTheDocument();

    rerender(<DynamicIcon iconName="AddFilled" />);
    expect(screen.getByTestId('add-icon')).toBeInTheDocument();
    expect(screen.queryByTestId('checkmark-icon')).not.toBeInTheDocument();
  });

  it('updates size when size prop changes', () => {
    const { rerender } = render(
      <DynamicIcon iconName="CheckmarkFilled" size={24} />
    );
    expect(screen.getByTestId('checkmark-icon')).toHaveAttribute(
      'data-size',
      '24'
    );

    rerender(<DynamicIcon iconName="CheckmarkFilled" size={48} />);
    expect(screen.getByTestId('checkmark-icon')).toHaveAttribute(
      'data-size',
      '48'
    );
  });

  it('renders icon as SVG element', () => {
    const { container } = render(<DynamicIcon iconName="CheckmarkFilled" />);
    expect(container.querySelector('svg')).toBeInTheDocument();
  });

  it('handles rapid icon name changes', () => {
    const { rerender } = render(<DynamicIcon iconName="CheckmarkFilled" />);
    rerender(<DynamicIcon iconName="CloseFilled" />);
    rerender(<DynamicIcon iconName="AddFilled" />);
    rerender(<DynamicIcon iconName="Error" />);
    expect(screen.getByTestId('error-icon')).toBeInTheDocument();
  });
});
