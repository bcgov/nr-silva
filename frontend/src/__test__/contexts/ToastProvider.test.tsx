import { fireEvent, render, screen } from '@testing-library/react';
import { toast } from 'react-toastify';
import { describe, expect, it, vi } from 'vitest';

import ToastProvider from '@/contexts/ToastProvider';

vi.mock('react-toastify', () => ({
  toast: {
    dismiss: vi.fn(),
  },
  ToastContainer: (props: Record<string, unknown>) => (
    <div
      data-auto-close={props.autoClose}
      data-close-button={String(props.closeButton)}
      data-newest-on-top={String(props.newestOnTop)}
      data-position={props.position}
      data-testid="toast-container"
    />
  ),
}));

describe('ToastProvider', () => {
  it('renders its children and the configured toast container', () => {
    render(
      <ToastProvider>
        <p>Application content</p>
      </ToastProvider>
    );

    expect(screen.getByText('Application content')).toBeInTheDocument();
    expect(screen.getByTestId('toast-container')).toHaveAttribute('data-position', 'top-right');
    expect(screen.getByTestId('toast-container')).toHaveAttribute('data-auto-close', '5000');
    expect(screen.getByTestId('toast-container')).toHaveAttribute('data-close-button', 'true');
    expect(screen.getByTestId('toast-container')).toHaveAttribute('data-newest-on-top', 'true');
  });

  it('dismisses active toasts when Escape is pressed', () => {
    render(
      <ToastProvider>
        <p>Application content</p>
      </ToastProvider>
    );

    fireEvent.keyDown(document, { key: 'Escape' });

    expect(toast.dismiss).toHaveBeenCalledOnce();
  });
});
