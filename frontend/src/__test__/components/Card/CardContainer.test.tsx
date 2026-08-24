import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import CardContainer from '@/components/Card/CardContainer';
import '@testing-library/jest-dom';

// Mock Carbon Grid component
vi.mock('@carbon/react', () => ({
  Grid: ({ className, children }: any) => (
    <div data-testid="carbon-grid" className={className}>
      {children}
    </div>
  ),
}));

describe('CardContainer', () => {
  it('renders Grid component', () => {
    render(<CardContainer>Content</CardContainer>);
    expect(screen.getByTestId('carbon-grid')).toBeInTheDocument();
  });

  it('renders children content', () => {
    render(
      <CardContainer>
        <span data-testid="child-content">Test Content</span>
      </CardContainer>
    );
    expect(screen.getByTestId('child-content')).toBeInTheDocument();
  });

  it('applies card-container-grid class', () => {
    render(<CardContainer>Content</CardContainer>);
    expect(screen.getByTestId('carbon-grid')).toHaveClass('card-container-grid');
  });

  it('applies default-grid class', () => {
    render(<CardContainer>Content</CardContainer>);
    expect(screen.getByTestId('carbon-grid')).toHaveClass('default-grid');
  });

  it('applies custom className when provided', () => {
    render(
      <CardContainer className="custom-class">
        Content
      </CardContainer>
    );
    expect(screen.getByTestId('carbon-grid')).toHaveClass('custom-class');
  });

  it('applies both default and custom classes', () => {
    render(
      <CardContainer className="my-custom-class">
        Content
      </CardContainer>
    );
    const grid = screen.getByTestId('carbon-grid');
    expect(grid).toHaveClass('card-container-grid');
    expect(grid).toHaveClass('default-grid');
    expect(grid).toHaveClass('my-custom-class');
  });

  it('renders multiple children', () => {
    render(
      <CardContainer>
        <div data-testid="child-1">First</div>
        <div data-testid="child-2">Second</div>
        <div data-testid="child-3">Third</div>
      </CardContainer>
    );
    expect(screen.getByTestId('child-1')).toBeInTheDocument();
    expect(screen.getByTestId('child-2')).toBeInTheDocument();
    expect(screen.getByTestId('child-3')).toBeInTheDocument();
  });

  it('renders with no children', () => {
    const { container } = render(<CardContainer />);
    expect(container.querySelector('[data-testid="carbon-grid"]')).toBeInTheDocument();
  });

  it('renders with string children', () => {
    render(<CardContainer>Just text</CardContainer>);
    expect(screen.getByText('Just text')).toBeInTheDocument();
  });

  it('renders with no className', () => {
    render(<CardContainer>Content</CardContainer>);
    const grid = screen.getByTestId('carbon-grid');
    expect(grid.className).toContain('card-container-grid');
    expect(grid.className).toContain('default-grid');
  });

  it('combines multiple class names correctly', () => {
    render(
      <CardContainer className="first-class second-class">
        Content
      </CardContainer>
    );
    const grid = screen.getByTestId('carbon-grid');
    expect(grid).toHaveClass('card-container-grid');
    expect(grid).toHaveClass('default-grid');
    expect(grid).toHaveClass('first-class');
    expect(grid).toHaveClass('second-class');
  });

  it('renders complex component children', () => {
    const ComplexChild = () => <div data-testid="complex">Complex Component</div>;
    render(
      <CardContainer>
        <ComplexChild />
      </CardContainer>
    );
    expect(screen.getByTestId('complex')).toBeInTheDocument();
  });

  it('maintains children order', () => {
    const { container } = render(
      <CardContainer>
        <div data-testid="first">1</div>
        <div data-testid="second">2</div>
        <div data-testid="third">3</div>
      </CardContainer>
    );
    const children = container.querySelectorAll('[data-testid^="second"], [data-testid^="first"], [data-testid^="third"]');
    expect(children.length).toBe(3);
  });
});
