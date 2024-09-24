import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import AvatarImage from '../../components/AvatarImage';

describe('AvatarImage component tests', () => {
  it('should render the avatar image component correctly', () => {
    render(
      <AvatarImage userName="John Doe" size="large" />
    );

    const element = screen.getByText('JD');
    expect(element).toBeDefined();
  });
});
