import { toast } from 'react-toastify';
import { describe, expect, it, vi } from 'vitest';

import { showToast } from '@/utils/Toast';

vi.mock('react-toastify', () => ({
  toast: {
    success: vi.fn(),
    error: vi.fn(),
    warning: vi.fn(),
    info: vi.fn(),
  },
}));

describe('showToast', () => {
  it.each([
    ['success', toast.success],
    ['error', toast.error],
    ['warning', toast.warning],
    ['info', toast.info],
  ] as const)('shows a %s toast with the supplied message', (type, toastMethod) => {
    showToast[type]('Toast message');

    expect(toastMethod).toHaveBeenCalledWith('Toast message');
  });
});
