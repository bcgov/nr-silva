import { fireEvent, render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';

import LeavePageModal from '@/components/Modals/LeavePageModal';

describe('LeavePageModal', () => {
  it('uses the supplied workflow name and delegates each user action', () => {
    const onLeave = vi.fn();
    const onRequestClose = vi.fn();
    const onStay = vi.fn();

    render(
      <LeavePageModal
        open
        helperTop="Edit tenure information"
        onLeave={onLeave}
        onRequestClose={onRequestClose}
        onStay={onStay}
      />
    );

    expect(screen.getByText('Edit tenure information')).toBeInTheDocument();
    fireEvent.click(screen.getByRole('button', { name: 'Stay on this page' }));
    fireEvent.click(screen.getByRole('button', { name: 'Leave page' }));

    expect(onStay).toHaveBeenCalledOnce();
    expect(onLeave).toHaveBeenCalledOnce();
    expect(onRequestClose).not.toHaveBeenCalled();
  });
});
