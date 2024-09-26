import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import Icon from '../../components/Icon';

describe('Icon Component', () => {
  it('should render the correct Carbon icon', () => {
    render(<Icon iconName="Add" />);
    
    // Assuming CarbonIcons[iconName] returns the correct icon component
    expect(screen.getByTestId('carbon-icon-Add')).toBeDefined();
  });
});
