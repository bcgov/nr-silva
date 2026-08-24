import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import DisplayField from '../../components/DisplayField';
import '@testing-library/jest-dom';

describe('DisplayField Component', () => {
  it('renders label and value correctly', () => {
    render(<DisplayField label="Test Label" value="Test Value" />);
    expect(screen.getByText('Test Label')).toBeInTheDocument();
    expect(screen.getByText('Test Value')).toBeInTheDocument();
  });

  it('renders numeric value correctly', () => {
    render(<DisplayField label="Count" value={42} />);
    expect(screen.getByText('Count')).toBeInTheDocument();
    expect(screen.getByText('42')).toBeInTheDocument();
  });

  it('displays placeholder when value is null', () => {
    const { container } = render(<DisplayField label="Empty Field" value={null} />);
    expect(screen.getByText('Empty Field')).toBeInTheDocument();
    const valueSpan = container.querySelector('.display-field-value');
    expect(valueSpan?.textContent).toBe('--');
  });

  it('displays placeholder when value is undefined', () => {
    const { container } = render(<DisplayField label="Undefined Field" value={undefined as any} />);
    expect(screen.getByText('Undefined Field')).toBeInTheDocument();
    const valueSpan = container.querySelector('.display-field-value');
    expect(valueSpan?.textContent).toBe('--');
  });

  it('renders required indicator when required is true', () => {
    const { container } = render(<DisplayField label="Required Field" value="Value" required={true} />);
    const requiredSpan = container.querySelector('.display-field-required');
    expect(requiredSpan).toBeInTheDocument();
    expect(requiredSpan).toHaveTextContent('*');
  });

  it('does not render required indicator when required is false', () => {
    const { container } = render(<DisplayField label="Optional Field" value="Value" required={false} />);
    const requiredSpan = container.querySelector('.display-field-required');
    expect(requiredSpan).not.toBeInTheDocument();
  });

  it('does not render required indicator when required is not provided', () => {
    const { container } = render(<DisplayField label="Default Field" value="Value" />);
    const requiredSpan = container.querySelector('.display-field-required');
    expect(requiredSpan).not.toBeInTheDocument();
  });

  it('renders zero value correctly', () => {
    render(<DisplayField label="Zero" value={0} />);
    expect(screen.getByText('0')).toBeInTheDocument();
  });

  it('renders empty string as empty', () => {
    const { container } = render(<DisplayField label="Empty String" value="" />);
    const valueSpan = container.querySelector('.display-field-value');
    expect(valueSpan?.textContent).toBe('');
  });

  it('applies correct CSS classes', () => {
    const { container } = render(<DisplayField label="Test" value="Value" />);
    expect(container.querySelector('.display-field-container')).toBeInTheDocument();
    expect(container.querySelector('.display-field-label')).toBeInTheDocument();
    expect(container.querySelector('.display-field-value')).toBeInTheDocument();
  });
});
