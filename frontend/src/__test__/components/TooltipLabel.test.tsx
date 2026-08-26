import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import TooltipLabel from '@/components/TooltipLabel';
import '@testing-library/jest-dom';

// Mock Carbon components
vi.mock('@carbon/react', async () => {
  const actual = await vi.importActual('@carbon/react');
  return {
    ...actual,
    Tooltip: ({ children, label, align, autoAlign }: any) => (
      <div data-testid="mock-tooltip" data-label={label} data-align={align} data-auto-align={autoAlign}>
        <div data-testid="tooltip-label">{label}</div>
        {children}
      </div>
    ),
  };
});

// Mock Information icon
vi.mock('@carbon/icons-react', async () => {
  const actual = await vi.importActual('@carbon/icons-react');
  return {
    ...actual,
    Information: () => <span data-testid="info-icon">ℹ</span>,
  };
});

describe('TooltipLabel', () => {
  it('renders label text', () => {
    render(
      <TooltipLabel
        label="Test Label"
        tooltip="Test Tooltip"
      />
    );
    expect(screen.getByText('Test Label')).toBeInTheDocument();
  });

  it('renders tooltip content', () => {
    render(
      <TooltipLabel
        label="Label"
        tooltip="Tooltip Content"
      />
    );
    expect(screen.getByText('Tooltip Content')).toBeInTheDocument();
  });

  it('applies silva-tooltip-label class', () => {
    const { container } = render(
      <TooltipLabel label="Label" tooltip="Tooltip" />
    );
    expect(container.querySelector('.silva-tooltip-label')).toBeInTheDocument();
  });

  it('renders information icon', () => {
    render(
      <TooltipLabel label="Label" tooltip="Tooltip" />
    );
    expect(screen.getByTestId('info-icon')).toBeInTheDocument();
  });

  it('applies default-label class by default', () => {
    const { container } = render(
      <TooltipLabel label="Label" tooltip="Tooltip" />
    );
    expect(container.querySelector('label.default-label')).toBeInTheDocument();
  });

  it('applies default-label-02 class when useLabel02 is true', () => {
    const { container } = render(
      <TooltipLabel
        label="Label"
        tooltip="Tooltip"
        useLabel02={true}
      />
    );
    expect(container.querySelector('label.default-label-02')).toBeInTheDocument();
  });

  it('applies custom className', () => {
    const { container } = render(
      <TooltipLabel
        label="Label"
        tooltip="Tooltip"
        className="custom-class"
      />
    );
    const div = container.querySelector('.silva-tooltip-label');
    expect(div?.className).toContain('custom-class');
  });

  it('sets htmlFor attribute on label', () => {
    const { container } = render(
      <TooltipLabel
        label="Label"
        tooltip="Tooltip"
        htmlFor="input-id"
      />
    );
    const label = container.querySelector('label');
    expect(label).toHaveAttribute('for', 'input-id');
  });

  it('sets id attribute on container', () => {
    const { container } = render(
      <TooltipLabel
        label="Label"
        tooltip="Tooltip"
        id="container-id"
      />
    );
    const div = container.querySelector('#container-id');
    expect(div).toBeInTheDocument();
  });

  it('passes align prop to tooltip', () => {
    render(
      <TooltipLabel
        label="Label"
        tooltip="Tooltip"
        align="top"
      />
    );
    const tooltip = screen.getByTestId('mock-tooltip');
    expect(tooltip).toHaveAttribute('data-align', 'top');
  });

  it('passes autoAlign prop to tooltip', () => {
    render(
      <TooltipLabel
        label="Label"
        tooltip="Tooltip"
        autoAlign={true}
      />
    );
    const tooltip = screen.getByTestId('mock-tooltip');
    expect(tooltip).toHaveAttribute('data-auto-align', 'true');
  });

  it('renders label inside default-label class', () => {
    render(
      <TooltipLabel
        label="My Label"
        tooltip="Tooltip"
      />
    );
    expect(screen.getByText('My Label')).toBeInTheDocument();
  });

  it('renders with tooltip as JSX', () => {
    const tooltipContent = <span>Custom <strong>Tooltip</strong></span>;
    render(
      <TooltipLabel
        label="Label"
        tooltip={tooltipContent}
      />
    );
    expect(screen.getByText('Tooltip')).toBeInTheDocument();
  });

  it('combines label class correctly when useLabel02 false', () => {
    const { container } = render(
      <TooltipLabel
        label="Label"
        tooltip="Tooltip"
        useLabel02={false}
      />
    );
    const label = container.querySelector('label');
    expect(label).toHaveClass('default-label');
  });
});
