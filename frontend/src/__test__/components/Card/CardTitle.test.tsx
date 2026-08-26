import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import CardTitle from '@/components/Card/CardTitle';
import '@testing-library/jest-dom';

describe('CardTitle', () => {
  it('renders title text', () => {
    render(<CardTitle title="Test Title" />);
    expect(screen.getByText('Test Title')).toBeInTheDocument();
  });

  it('renders h3 element for title', () => {
    render(<CardTitle title="My Title" />);
    const heading = screen.getByRole('heading', { level: 3 });
    expect(heading).toHaveTextContent('My Title');
  });

  it('applies card-title class to heading', () => {
    render(<CardTitle title="Title" />);
    const heading = screen.getByRole('heading', { level: 3 });
    expect(heading).toHaveClass('card-title');
  });

  it('renders section with card-title-section class', () => {
    const { container } = render(<CardTitle title="Title" />);
    expect(container.querySelector('.card-title-section')).toBeInTheDocument();
  });

  it('renders subtitle when provided', () => {
    render(
      <CardTitle
        title="Title"
        subtitle="Subtitle Text"
      />
    );
    expect(screen.getByText('Subtitle Text')).toBeInTheDocument();
  });

  it('does not render subtitle when not provided', () => {
    render(<CardTitle title="Title" />);
    expect(screen.queryByText('Subtitle')).not.toBeInTheDocument();
  });

  it('renders subtitle as h5 element', () => {
    render(
      <CardTitle
        title="Title"
        subtitle="My Subtitle"
      />
    );
    const heading = screen.getByRole('heading', { level: 5 });
    expect(heading).toHaveTextContent('My Subtitle');
  });

  it('applies card-subtitle class to subtitle', () => {
    render(
      <CardTitle
        title="Title"
        subtitle="Subtitle"
      />
    );
    const subtitle = screen.getByText('Subtitle');
    expect(subtitle).toHaveClass('card-subtitle');
  });

  it('applies id attribute to title heading', () => {
    render(
      <CardTitle
        title="Title"
        id="section-title"
      />
    );
    const heading = screen.getByRole('heading', { level: 3 });
    expect(heading).toHaveAttribute('id', 'section-title');
  });

  it('does not apply id when not provided', () => {
    render(<CardTitle title="Title" />);
    const heading = screen.getByRole('heading', { level: 3 });
    expect(heading).not.toHaveAttribute('id');
  });

  it('renders title and subtitle together', () => {
    render(
      <CardTitle
        title="Main Title"
        subtitle="Sub Title"
      />
    );
    expect(screen.getByText('Main Title')).toBeInTheDocument();
    expect(screen.getByText('Sub Title')).toBeInTheDocument();
  });

  it('renders with all props provided', () => {
    render(
      <CardTitle
        title="Full Title"
        subtitle="Full Subtitle"
        id="full-id"
      />
    );
    const titleHeading = screen.getByRole('heading', { level: 3 });
    const subtitleHeading = screen.getByRole('heading', { level: 5 });

    expect(titleHeading).toHaveTextContent('Full Title');
    expect(titleHeading).toHaveAttribute('id', 'full-id');
    expect(subtitleHeading).toHaveTextContent('Full Subtitle');
  });

  it('renders in correct DOM structure', () => {
    const { container } = render(
      <CardTitle
        title="Title"
        subtitle="Subtitle"
        id="my-id"
      />
    );
    const section = container.querySelector('.card-title-section');
    expect(section).toBeInTheDocument();

    const h3 = section?.querySelector('h3.card-title');
    const h5 = section?.querySelector('h5.card-subtitle');

    expect(h3).toBeInTheDocument();
    expect(h5).toBeInTheDocument();
  });

  it('renders subtitle only when subtitle prop is defined', () => {
    const { rerender, container: container1 } = render(
      <CardTitle title="Title" />
    );
    expect(container1.querySelector('h5')).not.toBeInTheDocument();

    rerender(<CardTitle title="Title" subtitle="Now with subtitle" />);
    expect(screen.getByText('Now with subtitle')).toBeInTheDocument();
  });

  it('handles empty title gracefully', () => {
    render(<CardTitle title="" />);
    expect(screen.getByRole('heading', { level: 3 })).toBeInTheDocument();
  });

  it('handles empty subtitle gracefully', () => {
    render(
      <CardTitle
        title="Title"
        subtitle=""
      />
    );
    // Should render because empty string is falsy
    expect(screen.queryByRole('heading', { level: 5 })).not.toBeInTheDocument();
  });
});
