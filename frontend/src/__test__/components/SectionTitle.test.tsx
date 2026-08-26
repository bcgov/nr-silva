import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import SectionTitle from '@/components/SectionTitle';
import '@testing-library/jest-dom';

// Mock Subtitle component
vi.mock('@/components/Subtitle', () => ({
  default: ({ text }: { text: string | React.ReactNode }) => (
    <div data-testid="mock-subtitle">{text}</div>
  ),
}));

// Mock Carbon's Column component
vi.mock('@carbon/react', async () => {
  const actual = await vi.importActual('@carbon/react');
  return {
    ...actual,
    Column: ({ children, className }: any) => (
      <div className={className} data-testid="mock-column">
        {children}
      </div>
    ),
  };
});

describe('SectionTitle', () => {
  it('renders title', () => {
    render(
      <SectionTitle
        title="Test Title"
        subtitle="Test Subtitle"
      />
    );
    expect(screen.getByText('Test Title')).toBeInTheDocument();
  });

  it('renders h6 element with section-title class', () => {
    const { container } = render(
      <SectionTitle title="Title" subtitle="Subtitle" />
    );
    expect(container.querySelector('h6.section-title')).toBeInTheDocument();
  });

  it('renders Subtitle component', () => {
    render(
      <SectionTitle
        title="Title"
        subtitle="Sub"
      />
    );
    expect(screen.getByTestId('mock-subtitle')).toBeInTheDocument();
  });

  it('passes subtitle text to Subtitle component', () => {
    render(
      <SectionTitle
        title="Title"
        subtitle="My Subtitle"
      />
    );
    expect(screen.getByText('My Subtitle')).toBeInTheDocument();
  });

  it('renders Column with section-title-container class', () => {
    render(
      <SectionTitle
        title="Title"
        subtitle="Subtitle"
      />
    );
    expect(screen.getByTestId('mock-column')).toHaveClass('section-title-container');
  });

  it('renders long title text', () => {
    const longTitle = 'This is a very long section title';
    render(
      <SectionTitle
        title={longTitle}
        subtitle="Subtitle"
      />
    );
    expect(screen.getByText(longTitle)).toBeInTheDocument();
  });

  it('renders long subtitle text', () => {
    const longSubtitle = 'This is a very long subtitle description';
    render(
      <SectionTitle
        title="Title"
        subtitle={longSubtitle}
      />
    );
    expect(screen.getByText(longSubtitle)).toBeInTheDocument();
  });

  it('renders with special characters in title', () => {
    render(
      <SectionTitle
        title="Title @#$%"
        subtitle="Subtitle"
      />
    );
    expect(screen.getByText('Title @#$%')).toBeInTheDocument();
  });

  it('renders with special characters in subtitle', () => {
    render(
      <SectionTitle
        title="Title"
        subtitle="Subtitle @#$%"
      />
    );
    expect(screen.getByText('Subtitle @#$%')).toBeInTheDocument();
  });

  it('renders title and subtitle together', () => {
    render(
      <SectionTitle
        title="Main Title"
        subtitle="Secondary Text"
      />
    );
    expect(screen.getByText('Main Title')).toBeInTheDocument();
    expect(screen.getByText('Secondary Text')).toBeInTheDocument();
  });

  it('does not render enableFavourite or activity props in output', () => {
    const { container } = render(
      <SectionTitle
        title="Title"
        subtitle="Subtitle"
        enableFavourite={true}
        activity="test activity"
      />
    );
    expect(container.textContent).not.toContain('test activity');
  });

  it('renders with numeric title', () => {
    render(
      <SectionTitle
        title="123"
        subtitle="456"
      />
    );
    expect(screen.getByText('123')).toBeInTheDocument();
    expect(screen.getByText('456')).toBeInTheDocument();
  });
});
