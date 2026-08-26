import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import Subtitle from '@/components/Subtitle';
import '@testing-library/jest-dom';

describe('Subtitle', () => {
  it('renders subtitle with text', () => {
    render(<Subtitle text="Test Subtitle" />);
    expect(screen.getByText('Test Subtitle')).toBeInTheDocument();
  });

  it('applies default subtitle-section class', () => {
    const { container } = render(<Subtitle text="Subtitle" />);
    expect(container.querySelector('p.subtitle-section')).toBeInTheDocument();
  });

  it('renders p element', () => {
    const { container } = render(<Subtitle text="Text" />);
    expect(container.querySelector('p')).toBeInTheDocument();
  });

  it('applies custom className with default class', () => {
    const { container } = render(<Subtitle text="Text" className="custom-class" />);
    expect(container.querySelector('p.custom-class.subtitle-section')).toBeInTheDocument();
  });

  it('renders text with custom className', () => {
    render(<Subtitle text="My Text" className="special" />);
    expect(screen.getByText('My Text')).toBeInTheDocument();
  });

  it('does not apply custom className if undefined', () => {
    const { container } = render(<Subtitle text="Text" />);
    const p = container.querySelector('p');
    expect(p?.className).toBe('subtitle-section');
  });

  it('renders long text', () => {
    const longText = 'This is a very long subtitle that might wrap to multiple lines in the UI';
    render(<Subtitle text={longText} />);
    expect(screen.getByText(longText)).toBeInTheDocument();
  });

  it('renders React node as text', () => {
    const node = <span data-testid="custom-span">Content</span>;
    const { container } = render(<Subtitle text={node} />);
    expect(container.querySelector('[data-testid="custom-span"]')).toBeInTheDocument();
  });

  it('renders empty string', () => {
    const { container } = render(<Subtitle text="" />);
    const p = container.querySelector('p');
    expect(p?.textContent).toBe('');
  });

  it('renders special characters in text', () => {
    render(<Subtitle text="Text with @#$% characters" />);
    expect(screen.getByText('Text with @#$% characters')).toBeInTheDocument();
  });

  it('renders numeric text', () => {
    render(<Subtitle text="123" />);
    expect(screen.getByText('123')).toBeInTheDocument();
  });

  it('combines className correctly', () => {
    const { container } = render(<Subtitle text="Text" className="prefix" />);
    const p = container.querySelector('p');
    expect(p?.className).toBe('prefix subtitle-section');
  });

  it('renders with multiple custom classes', () => {
    const { container } = render(<Subtitle text="Text" className="class1 class2" />);
    const p = container.querySelector('p');
    expect(p?.className).toContain('class1 class2');
    expect(p?.className).toContain('subtitle-section');
  });

  it('renders text as direct child of p element', () => {
    const { container } = render(<Subtitle text="Content" />);
    const p = container.querySelector('p');
    expect(p?.textContent).toBe('Content');
  });

  it('renders JSX node with className', () => {
    const node = <strong>Important</strong>;
    const { container } = render(<Subtitle text={node} className="highlight" />);
    expect(container.querySelector('strong')).toBeInTheDocument();
    expect(container.querySelector('p.highlight.subtitle-section')).toBeInTheDocument();
  });
});
