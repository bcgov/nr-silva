import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import OpeningsMapEntryPopup from '../../components/OpeningsMapEntryPopup';

describe('OpeningsMapEntryPopup', () => {
  it('renders correctly with given openingId', () => {
    render(<OpeningsMapEntryPopup openingId={123} />);
    expect(screen.getByText('Opening ID: 123')).toBeInTheDocument();
  });
});
