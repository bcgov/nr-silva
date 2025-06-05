import React from 'react';
import { render } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import OpeningStatusTag from '@/components/OpeningStatusTag';
import { CodeDescriptionDto } from '@/types/OpenApiTypes';

describe('Status Tag component tests', () => {
  it('should have completed color status tag rendered', () => {
    const status: CodeDescriptionDto = { code: "APP", description: "Approved" };

    const { getByTestId } = render(
      <OpeningStatusTag status={status} />
    );

    const element: HTMLElement | null = getByTestId(`tag__status_colored_tag_${"green"}`);
    expect(element).toBeDefined();
  });
});
