import { render, screen, fireEvent } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import FeatureUnavailable from '@/components/FeatureUnavailable';
import '@testing-library/jest-dom';

// Mock dependencies
vi.mock('@carbon/react', () => ({
  Button: ({
    children,
    onClick,
    className,
    kind,
  }: any) => (
    <button className={className} data-kind={kind} onClick={onClick}>
      {children}
    </button>
  ),
}));

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: vi.fn(() => mockNavigate),
  };
});

vi.mock('@/components/EmptySection', () => ({
  default: ({ icon, title, description }: any) => (
    <div data-testid="empty-section" data-icon={icon}>
      <h2>{title}</h2>
      <p>{description}</p>
    </div>
  ),
}));

vi.mock('@/routes/config', () => ({
  DashboardRoute: {
    path: '/dashboard',
  },
}));

describe('FeatureUnavailable', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });
  it('renders container with feature-unavailable-container class', () => {
    const { container } = render(
      <FeatureUnavailable featureName="TestFeature" />
    );
    expect(
      container.querySelector('.feature-unavailable-container')
    ).toBeInTheDocument();
  });

  it('renders EmptySection component', () => {
    render(<FeatureUnavailable featureName="TestFeature" />);
    expect(screen.getByTestId('empty-section')).toBeInTheDocument();
  });

  it('renders default title using featureName', () => {
    render(<FeatureUnavailable featureName="MyFeature" />);
    expect(screen.getByText('MyFeature is not available')).toBeInTheDocument();
  });

  it('renders custom title when provided', () => {
    render(
      <FeatureUnavailable
        featureName="MyFeature"
        title="Custom Title"
      />
    );
    expect(screen.getByText('Custom Title')).toBeInTheDocument();
    expect(
      screen.queryByText('MyFeature is not available')
    ).not.toBeInTheDocument();
  });

  it('renders default description', () => {
    render(<FeatureUnavailable featureName="TestFeature" />);
    expect(
      screen.getByText('This feature is not currently available.')
    ).toBeInTheDocument();
  });

  it('renders custom description when provided', () => {
    render(
      <FeatureUnavailable
        featureName="TestFeature"
        description="Custom description text"
      />
    );
    expect(screen.getByText('Custom description text')).toBeInTheDocument();
  });

  it('passes icon "Construction" to EmptySection', () => {
    render(<FeatureUnavailable featureName="TestFeature" />);
    expect(screen.getByTestId('empty-section')).toHaveAttribute(
      'data-icon',
      'Construction'
    );
  });

  it('renders primary button by default', () => {
    const { container } = render(
      <FeatureUnavailable featureName="TestFeature" />
    );
    const button = container.querySelector('button');
    expect(button).toHaveAttribute('data-kind', 'primary');
  });

  it('renders default action button label', () => {
    render(<FeatureUnavailable featureName="TestFeature" />);
    expect(screen.getByText('Back to dashboard')).toBeInTheDocument();
  });

  it('renders custom action button label', () => {
    render(
      <FeatureUnavailable
        featureName="TestFeature"
        actionLabel="Go Home"
      />
    );
    expect(screen.getByText('Go Home')).toBeInTheDocument();
  });

  it('applies feature-unavailable-button class to button', () => {
    const { container } = render(
      <FeatureUnavailable featureName="TestFeature" />
    );
    const button = container.querySelector('.feature-unavailable-button');
    expect(button).toBeInTheDocument();
  });

  it('calls onActionClick when button is clicked', () => {
    const mockOnClick = vi.fn();
    render(
      <FeatureUnavailable
        featureName="TestFeature"
        onActionClick={mockOnClick}
      />
    );
    fireEvent.click(screen.getByText('Back to dashboard'));
    expect(mockOnClick).toHaveBeenCalled();
  });

  it('navigates to dashboard when button clicked without custom handler', () => {
    render(<FeatureUnavailable featureName="TestFeature" />);
    fireEvent.click(screen.getByText('Back to dashboard'));
    expect(mockNavigate).toHaveBeenCalledWith('/dashboard');
  });

  it('prefers onActionClick over navigate', () => {
    const mockOnClick = vi.fn();

    render(
      <FeatureUnavailable
        featureName="TestFeature"
        onActionClick={mockOnClick}
      />
    );
    fireEvent.click(screen.getByText('Back to dashboard'));
    expect(mockOnClick).toHaveBeenCalled();
    expect(mockNavigate).not.toHaveBeenCalled();
  });

  it('applies custom className to container', () => {
    const { container } = render(
      <FeatureUnavailable
        featureName="TestFeature"
        className="custom-class"
      />
    );
    expect(
      container.querySelector('.feature-unavailable-container.custom-class')
    ).toBeInTheDocument();
  });

  it('applies both default and custom className', () => {
    const { container } = render(
      <FeatureUnavailable
        featureName="TestFeature"
        className="my-custom-class"
      />
    );
    const containerDiv = container.querySelector('.feature-unavailable-container');
    expect(containerDiv?.className).toContain('feature-unavailable-container');
    expect(containerDiv?.className).toContain('my-custom-class');
  });

  it('handles custom description as ReactNode', () => {
    render(
      <FeatureUnavailable
        featureName="TestFeature"
        description={<span data-testid="custom-desc">Custom ReactNode</span>}
      />
    );
    expect(screen.getByTestId('custom-desc')).toBeInTheDocument();
  });

  it('renders all props together', () => {
    const mockOnClick = vi.fn();
    render(
      <FeatureUnavailable
        featureName="PaymentFeature"
        title="Payment Processing Unavailable"
        description="We're working on payment processing."
        actionLabel="Return to Home"
        onActionClick={mockOnClick}
        className="payment-feature"
      />
    );
    expect(
      screen.getByText('Payment Processing Unavailable')
    ).toBeInTheDocument();
    expect(
      screen.getByText('We\'re working on payment processing.')
    ).toBeInTheDocument();
    expect(screen.getByText('Return to Home')).toBeInTheDocument();
  });

  it('has correct button structure', () => {
    const { container } = render(
      <FeatureUnavailable featureName="TestFeature" />
    );
    const button = container.querySelector('button');
    expect(button?.className).toContain('feature-unavailable-button');
  });

  it('does not add extra spaces in className when no custom class', () => {
    const { container } = render(
      <FeatureUnavailable featureName="TestFeature" />
    );
    const containerDiv = container.querySelector('.feature-unavailable-container');
    // Should not have double spaces
    expect(containerDiv?.className).not.toMatch(/\s{2,}/);
  });

  it('handles empty featureName', () => {
    render(<FeatureUnavailable featureName="" />);
    // When featureName is empty, it renders " is not available"
    const titleHeading = document.querySelector('h2');
    expect(titleHeading).toBeInTheDocument();
  });

  it('renders EmptySection with correct props structure', () => {
    render(
      <FeatureUnavailable
        featureName="TestFeature"
        title="Test Title"
        description="Test Description"
      />
    );
    const emptySection = screen.getByTestId('empty-section');
    expect(emptySection).toBeInTheDocument();
    expect(emptySection).toHaveAttribute('data-icon', 'Construction');
  });
});
