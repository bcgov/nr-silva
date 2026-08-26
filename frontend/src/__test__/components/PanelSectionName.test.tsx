import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import PanelSectionName from '@/components/PanelSectionName';
import '@testing-library/jest-dom';

describe('PanelSectionName', () => {
  it('renders with title', () => {
    render(<PanelSectionName title="Test Title" />);
    expect(screen.getByText('Test Title')).toBeInTheDocument();
  });

  it('applies default panel-section class', () => {
    const { container } = render(<PanelSectionName title="Section" />);
    expect(container.querySelector('.panel-section')).toBeInTheDocument();
  });

  it('applies light class when light prop is true', () => {
    const { container } = render(<PanelSectionName title="Light Section" light={true} />);
    expect(container.querySelector('.panel-section-light')).toBeInTheDocument();
  });

  it('does not apply light class when light prop is false', () => {
    const { container } = render(<PanelSectionName title="Dark Section" light={false} />);
    expect(container.querySelector('.panel-section')).toBeInTheDocument();
    expect(container.querySelector('.panel-section-light')).not.toBeInTheDocument();
  });

  it('does not apply light class when light prop is undefined', () => {
    const { container } = render(<PanelSectionName title="Section" />);
    expect(container.querySelector('.panel-section')).toBeInTheDocument();
  });

  it('renders without title', () => {
    const { container } = render(<PanelSectionName />);
    expect(container.querySelector('span')).toBeInTheDocument();
  });

  it('renders span element', () => {
    const { container } = render(<PanelSectionName title="Test" />);
    expect(container.querySelector('span')).toBeInTheDocument();
  });

  it('renders title inside span', () => {
    const { container } = render(<PanelSectionName title="My Title" />);
    const span = container.querySelector('span');
    expect(span?.textContent).toBe('My Title');
  });

  it('renders with light class and title', () => {
    const { container } = render(<PanelSectionName title="Light Title" light={true} />);
    const div = container.querySelector('.panel-section-light');
    expect(div?.textContent).toBe('Light Title');
  });

  it('renders empty span when no title provided', () => {
    const { container } = render(<PanelSectionName />);
    const span = container.querySelector('span');
    expect(span?.textContent).toBe('');
  });

  it('renders with long title text', () => {
    const longTitle = 'This is a very long title that might wrap to multiple lines';
    render(<PanelSectionName title={longTitle} />);
    expect(screen.getByText(longTitle)).toBeInTheDocument();
  });

  it('renders with special characters in title', () => {
    render(<PanelSectionName title="Section @#$%" />);
    expect(screen.getByText('Section @#$%')).toBeInTheDocument();
  });

  it('renders with numeric title', () => {
    render(<PanelSectionName title="123" />);
    expect(screen.getByText('123')).toBeInTheDocument();
  });

  it('renders div with span child', () => {
    const { container } = render(<PanelSectionName title="Test" />);
    const div = container.querySelector('.panel-section');
    const span = div?.querySelector('span');
    expect(span).toBeInTheDocument();
  });
});
