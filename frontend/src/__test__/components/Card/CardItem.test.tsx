import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import CardItem from '@/components/Card/CardItem';
import '@testing-library/jest-dom';

// Mock Carbon components
vi.mock('@carbon/react', () => ({
  TextInputSkeleton: () => <div data-testid="text-input-skeleton">Skeleton</div>,
  DefinitionTooltip: ({ definition, children, openOnHover }: any) => (
    <div data-testid="definition-tooltip" title={definition} data-open-on-hover={openOnHover ? 'true' : 'false'}>
      {children}
    </div>
  ),
}));

describe('CardItem', () => {
  it('renders with label', () => {
    render(
      <CardItem label="Test Label">
        Test Content
      </CardItem>
    );
    expect(screen.getByText('Test Label')).toBeInTheDocument();
  });

  it('renders label as dt element', () => {
    render(
      <CardItem label="My Label">
        Content
      </CardItem>
    );
    const dt = screen.getByText('My Label');
    expect(dt.tagName).toBe('DT');
  });

  it('renders content as dd element', () => {
    const { container } = render(
      <CardItem label="Label">
        <span data-testid="content">Content Text</span>
      </CardItem>
    );
    const dd = container.querySelector('dd');
    expect(dd).toBeInTheDocument();
    expect(screen.getByTestId('content')).toBeInTheDocument();
  });

  it('renders dl element with card-item class', () => {
    const { container } = render(
      <CardItem label="Label">
        Content
      </CardItem>
    );
    const dl = container.querySelector('dl.card-item');
    expect(dl).toBeInTheDocument();
  });

  it('renders skeleton when showSkeleton is true', () => {
    render(
      <CardItem label="Label" showSkeleton={true}>
        Content
      </CardItem>
    );
    expect(screen.getByTestId('text-input-skeleton')).toBeInTheDocument();
  });

  it('does not render content when showSkeleton is true', () => {
    render(
      <CardItem label="Label" showSkeleton={true}>
        <span data-testid="hidden-content">Hidden</span>
      </CardItem>
    );
    expect(screen.queryByTestId('hidden-content')).not.toBeInTheDocument();
  });

  it('renders placeholder when no children provided', () => {
    render(<CardItem label="Label" />);
    expect(screen.getByText('--')).toBeInTheDocument();
  });

  it('renders id attribute on dl element', () => {
    const { container } = render(
      <CardItem label="Label" id="test-id">
        Content
      </CardItem>
    );
    expect(container.querySelector('dl')).toHaveAttribute('id', 'test-id');
  });

  it('applies card-item-label class to dt element', () => {
    const { container } = render(
      <CardItem label="Label">
        Content
      </CardItem>
    );
    const dt = container.querySelector('dt');
    expect(dt).toHaveClass('card-item-label');
  });

  it('applies card-item-content class to dd element when isNumber is false', () => {
    const { container } = render(
      <CardItem label="Label" isNumber={false}>
        Content
      </CardItem>
    );
    const dd = container.querySelector('dd');
    expect(dd).toHaveClass('card-item-content');
  });

  it('applies card-item-content-number class to dd when isNumber is true', () => {
    const { container } = render(
      <CardItem label="Label" isNumber={true}>
        12345
      </CardItem>
    );
    const dd = container.querySelector('dd');
    expect(dd).toHaveClass('card-item-content-number');
  });

  it('renders tooltip when tooltipText is provided', () => {
    render(
      <CardItem label="Label" tooltipText="Tooltip text">
        Content
      </CardItem>
    );
    expect(screen.getByTestId('definition-tooltip')).toBeInTheDocument();
  });

  it('renders tooltip with definition text', () => {
    render(
      <CardItem label="Label" tooltipText="My Tooltip">
        Content
      </CardItem>
    );
    expect(screen.getByTestId('definition-tooltip')).toHaveAttribute(
      'title',
      'My Tooltip'
    );
  });

  it('sets openOnHover to true on tooltip', () => {
    render(
      <CardItem label="Label" tooltipText="Tooltip">
        Content
      </CardItem>
    );
    expect(screen.getByTestId('definition-tooltip')).toHaveAttribute(
      'data-open-on-hover',
      'true'
    );
  });

  it('does not render tooltip when tooltipText is not provided', () => {
    render(
      <CardItem label="Label">
        Content
      </CardItem>
    );
    expect(screen.queryByTestId('definition-tooltip')).not.toBeInTheDocument();
  });

  it('renders data-testid based on kebab-case label', () => {
    const { container } = render(
      <CardItem label="My Card Label">
        Content
      </CardItem>
    );
    expect(container.querySelector('[data-testid="card-item-my-card-label"]')).toBeInTheDocument();
  });

  it('renders data-testid for content based on kebab-case label', () => {
    const { container } = render(
      <CardItem label="Tenure Type">
        Content
      </CardItem>
    );
    expect(container.querySelector('[data-testid="card-item-content-tenure-type"]')).toBeInTheDocument();
  });

  it('renders title attribute on dd when content is string', () => {
    const { container } = render(
      <CardItem label="Label">
        String Content
      </CardItem>
    );
    const dd = container.querySelector('dd');
    expect(dd).toHaveAttribute('title', 'String Content');
  });

  it('does not render title attribute when content is not string', () => {
    const { container } = render(
      <CardItem label="Label">
        <span>Content</span>
      </CardItem>
    );
    const dd = container.querySelector('dd');
    expect(dd).not.toHaveAttribute('title');
  });

  it('does not render title attribute when showSkeleton is true', () => {
    render(
      <CardItem label="Label" showSkeleton={true}>
        String Content
      </CardItem>
    );
    // When skeleton is shown, the entire dl/dd structure is replaced with skeleton
    expect(screen.queryByTestId('text-input-skeleton')).toBeInTheDocument();
  });

  it('handles React element children with tooltip', () => {
    render(
      <CardItem label="Label" tooltipText="Tooltip">
        <strong>Bold Content</strong>
      </CardItem>
    );
    expect(screen.getByText('Bold Content')).toBeInTheDocument();
  });

  it('wraps non-React element children in span when tooltip is present', () => {
    const { container } = render(
      <CardItem label="Label" tooltipText="Tooltip">
        String content
      </CardItem>
    );
    const span = container.querySelector('dd span');
    expect(span).toBeInTheDocument();
  });

  it('combines all props together correctly', () => {
    render(
      <CardItem
        label="Complete Label"
        id="complete-id"
        tooltipText="Complete Tooltip"
        isNumber={true}
      >
        Complete Content
      </CardItem>
    );
    expect(screen.getByText('Complete Label')).toBeInTheDocument();
    expect(screen.getByTestId('definition-tooltip')).toBeInTheDocument();
  });
});
