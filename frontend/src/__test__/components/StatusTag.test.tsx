import React from 'react';
import { render } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import StatusTag from '../../components/StatusTag';

describe('Status Tag component tests', () => {
  it('should have completed color status tag rendered', () => {
    const colorKey = 'Completed';

    const { getByTestId } = render(
      <StatusTag code={colorKey} />
    );

    const element: HTMLElement | null = getByTestId(`tag__status_colored_tag_${colorKey}`);
    expect(element).toBeDefined();
  });
});
