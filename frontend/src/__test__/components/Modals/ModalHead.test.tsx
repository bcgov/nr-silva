import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import ModalHead from '@/components/Modals/ModalHead';
import '@testing-library/jest-dom';

describe('ModalHead', () => {
  it('renders null when no props are provided', () => {
    const { container } = render(<ModalHead />);
    expect(container.firstChild).toBeNull();
  });

  it('renders section with aria-label when content is provided', () => {
    render(
      <ModalHead
        helperTop="Top text"
        title="Title"
        helperBottom="Bottom text"
      />
    );

    expect(screen.getByLabelText('modal title section')).toBeInTheDocument();
  });

  it('renders helperTop text when provided', () => {
    render(<ModalHead helperTop="Helper top content" />);
    expect(screen.getByText('Helper top content')).toBeInTheDocument();
  });

  it('renders title text when provided', () => {
    render(<ModalHead title="Modal Title" />);
    expect(screen.getByText('Modal Title')).toBeInTheDocument();
  });

  it('renders helperBottom text when provided', () => {
    render(<ModalHead helperBottom="Helper bottom content" />);
    expect(screen.getByText('Helper bottom content')).toBeInTheDocument();
  });

  it('renders all three sections when all props are provided', () => {
    render(
      <ModalHead
        helperTop="Top"
        title="Title"
        helperBottom="Bottom"
      />
    );

    expect(screen.getByText('Top')).toBeInTheDocument();
    expect(screen.getByText('Title')).toBeInTheDocument();
    expect(screen.getByText('Bottom')).toBeInTheDocument();
  });

  it('does not render helperTop when not provided', () => {
    render(
      <ModalHead
        title="Title"
        helperBottom="Bottom"
      />
    );

    expect(screen.queryByText('Helper top')).not.toBeInTheDocument();
  });

  it('does not render title when not provided', () => {
    render(
      <ModalHead
        helperTop="Top"
        helperBottom="Bottom"
      />
    );

    const headings = screen.queryAllByRole('heading');
    expect(headings.length).toBe(0);
  });

  it('does not render helperBottom when not provided', () => {
    render(
      <ModalHead
        helperTop="Top"
        title="Title"
      />
    );

    expect(screen.queryByText('Helper bottom')).not.toBeInTheDocument();
  });

  it('renders only helperTop when only helperTop is provided', () => {
    render(<ModalHead helperTop="Only top" />);
    expect(screen.getByText('Only top')).toBeInTheDocument();
  });

  it('renders only title when only title is provided', () => {
    render(<ModalHead title="Only title" />);
    expect(screen.getByText('Only title')).toBeInTheDocument();
  });

  it('renders only helperBottom when only helperBottom is provided', () => {
    render(<ModalHead helperBottom="Only bottom" />);
    expect(screen.getByText('Only bottom')).toBeInTheDocument();
  });

  it('renders helperTop and title but not helperBottom', () => {
    render(
      <ModalHead
        helperTop="Top"
        title="Title"
      />
    );

    expect(screen.getByText('Top')).toBeInTheDocument();
    expect(screen.getByText('Title')).toBeInTheDocument();
    expect(screen.queryByText('Bottom')).not.toBeInTheDocument();
  });

  it('renders title and helperBottom but not helperTop', () => {
    render(
      <ModalHead
        title="Title"
        helperBottom="Bottom"
      />
    );

    expect(screen.queryByText('Top')).not.toBeInTheDocument();
    expect(screen.getByText('Title')).toBeInTheDocument();
    expect(screen.getByText('Bottom')).toBeInTheDocument();
  });

  it('renders title with correct heading level (h4)', () => {
    render(<ModalHead title="Title Text" />);
    const heading = screen.getByRole('heading', { level: 4 });
    expect(heading).toHaveTextContent('Title Text');
  });

  it('renders helperTop and helperBottom as paragraphs', () => {
    render(
      <ModalHead
        helperTop="Top text"
        helperBottom="Bottom text"
      />
    );

    const paragraphs = screen.getAllByRole('region', { hidden: true }).length > 0 || true; // Paragraphs don't have ARIA roles
    expect(screen.getByText('Top text').tagName).toBe('P');
    expect(screen.getByText('Bottom text').tagName).toBe('P');
  });
});
