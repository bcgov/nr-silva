import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import EmptySection from '../../components/EmptySection';
import '@testing-library/jest-dom';

describe('EmptySection Component', () => {
  it('should render the empty section with icon', () => {
    render(<EmptySection icon="ErrorFilled" title="Test Title" description="Test Description" />);
    expect(screen.getByText('Test Title')).toBeDefined();
    expect(screen.getByText('Test Description')).toBeDefined();
    expect(screen.getByTestId('empty-section-icon')).toBeDefined();
  });

  it('should render the empty section with pictogram', () => {
    render(<EmptySection pictogram="Airplane" title="Airplane Title" description="Airplane Description" />);
    expect(screen.getByText('Airplane Title')).toBeDefined();
    expect(screen.getByText('Airplane Description')).toBeDefined();
    expect(screen.getByTestId('empty-section-icon')).toBeDefined();
  });

  it('should render the empty section with description as ReactNode', () => {
    const description = <span>Test Description</span>;
    render(<EmptySection icon="ErrorFilled" title="Test Title" description={description} />);
    expect(screen.getByText('Test Title')).toBeDefined();
    expect(screen.getByText('Test Description')).toBeDefined();
  });
});
