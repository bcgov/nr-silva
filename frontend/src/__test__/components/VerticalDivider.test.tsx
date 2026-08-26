import { render } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import VerticalDivider from '@/components/VerticalDivider';
import '@testing-library/jest-dom';

describe('VerticalDivider', () => {
  it('renders span element', () => {
    const { container } = render(<VerticalDivider />);
    expect(container.querySelector('span')).toBeInTheDocument();
  });

  it('applies inline-vertical-divider class', () => {
    const { container } = render(<VerticalDivider />);
    expect(container.querySelector('.inline-vertical-divider')).toBeInTheDocument();
  });

  it('renders as self-closing span', () => {
    const { container } = render(<VerticalDivider />);
    const span = container.querySelector('span');
    expect(span?.children.length).toBe(0);
  });

  it('renders multiple dividers independently', () => {
    const { container } = render(
      <>
        <VerticalDivider />
        <VerticalDivider />
      </>
    );
    const spans = container.querySelectorAll('.inline-vertical-divider');
    expect(spans.length).toBe(2);
  });

  it('has correct className', () => {
    const { container } = render(<VerticalDivider />);
    const span = container.querySelector('span');
    expect(span?.className).toBe('inline-vertical-divider');
  });

  it('renders without children', () => {
    const { container } = render(<VerticalDivider />);
    const span = container.querySelector('span');
    expect(span?.textContent).toBe('');
  });

  it('renders in sequential dividers without interference', () => {
    const { container } = render(
      <>
        <div className="item-one">Item 1</div>
        <VerticalDivider />
        <div className="item-two">Item 2</div>
      </>
    );
    const divider = container.querySelector('.inline-vertical-divider');
    expect(divider).toBeInTheDocument();
  });

  it('renders span with only className attribute', () => {
    const { container } = render(<VerticalDivider />);
    const span = container.querySelector('span');
    const attributes = span?.attributes;
    expect(attributes?.length).toBe(1);
    expect(attributes?.[0].name).toBe('class');
  });
});
