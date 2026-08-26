import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import RequiredLabel from '@/components/RequiredLabel';
import '@testing-library/jest-dom';

// Mock Carbon's Stack component
vi.mock('@carbon/react', async () => {
  const actual = await vi.importActual('@carbon/react');
  return {
    ...actual,
    Stack: ({ children, className, orientation, gap }: any) => (
      <div className={className} data-testid="mock-stack" data-orientation={orientation} data-gap={gap}>
        {children}
      </div>
    ),
  };
});

describe('RequiredLabel', () => {
  it('renders label element', () => {
    const { container } = render(
      <RequiredLabel>Field</RequiredLabel>
    );
    expect(container.querySelector('label')).toBeInTheDocument();
  });

  it('renders with required-label class', () => {
    const { container } = render(
      <RequiredLabel>Test Label</RequiredLabel>
    );
    expect(container.querySelector('label.required-label')).toBeInTheDocument();
  });

  it('renders with default-label class', () => {
    const { container } = render(
      <RequiredLabel>Test Label</RequiredLabel>
    );
    expect(container.querySelector('label.default-label')).toBeInTheDocument();
  });

  it('renders asterisk symbol', () => {
    const { container } = render(
      <RequiredLabel>Test Label</RequiredLabel>
    );
    expect(container.querySelector('.rhcp')).toHaveTextContent('*');
  });

  it('renders label text', () => {
    render(<RequiredLabel>My Label</RequiredLabel>);
    expect(screen.getByText('My Label')).toBeInTheDocument();
  });

  it('combines default classes with custom className', () => {
    const { container } = render(
      <RequiredLabel className="custom-class">Label</RequiredLabel>
    );
    const label = container.querySelector('label');
    expect(label?.className).toContain('required-label');
    expect(label?.className).toContain('default-label');
    expect(label?.className).toContain('custom-class');
  });

  it('renders Stack with horizontal orientation', () => {
    render(<RequiredLabel>Label</RequiredLabel>);
    expect(screen.getByTestId('mock-stack')).toHaveAttribute('data-orientation', 'horizontal');
  });

  it('renders Stack with gap 0.25rem', () => {
    render(<RequiredLabel>Label</RequiredLabel>);
    expect(screen.getByTestId('mock-stack')).toHaveAttribute('data-gap', '0.25rem');
  });

  it('renders with long label text', () => {
    const longText = 'This is a very long label that might wrap';
    render(<RequiredLabel>{longText}</RequiredLabel>);
    expect(screen.getByText(longText)).toBeInTheDocument();
  });

  it('forwards ref to label element', () => {
    const ref = { current: null };
    render(<RequiredLabel ref={ref}>Label</RequiredLabel>);
    expect(ref.current).toBeInstanceOf(HTMLLabelElement);
  });

  it('passes through additional attributes', () => {
    const { container } = render(
      <RequiredLabel data-testid="custom-label" htmlFor="input-id">
        Label
      </RequiredLabel>
    );
    const label = container.querySelector('[data-testid="custom-label"]');
    expect(label).toHaveAttribute('for', 'input-id');
  });

  it('renders asterisk and text separately', () => {
    const { container } = render(
      <RequiredLabel>Field Name</RequiredLabel>
    );
    const stack = screen.getByTestId('mock-stack');
    expect(stack.textContent).toContain('*');
    expect(stack.textContent).toContain('Field Name');
  });

  it('renders with empty className (filters out empty strings)', () => {
    const { container } = render(
      <RequiredLabel className="">Label</RequiredLabel>
    );
    const label = container.querySelector('label');
    expect(label?.className).toBe('required-label default-label');
  });
});
