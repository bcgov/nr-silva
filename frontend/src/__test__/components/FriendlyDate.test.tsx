import React from 'react';
import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import FriendlyDate from '../../components/FriendlyDate';

// Mock Tooltip component from Carbon to ensure tests run without extra dependencies
vi.mock('@carbon/react', () => {
  const Tooltip = ({ label, children }) => <span data-tooltip={label}>{children}</span>;
  return { Tooltip };
});

// Mock `Date.now` for consistent testing
beforeAll(() => {
  vi.useFakeTimers();
  vi.setSystemTime(new Date('2024-01-24T00:00:00')); // Set a fixed date for testing
});

afterAll(() => {
  vi.useRealTimers();
});

describe('FriendlyDate Component', () => {

  it('displays "Today" for today\'s date', () => {
    render(<FriendlyDate date="2024-01-24T06:23:12" />);
    expect(screen.getByText("in 6 hours")).toBeInTheDocument();
  });

  it('displays "Yesterday" for a date one day ago', () => {
    render(<FriendlyDate date="2024-01-23T00:00:00" />);
    expect(screen.getByText("yesterday")).toBeInTheDocument();
  });

  it('displays relative time within the last week', () => {
    render(<FriendlyDate date="2024-01-21T00:00:00" />);
    expect(screen.getByText("3 days ago")).toBeInTheDocument();
  });

  it('displays exact date for dates older than a week', () => {
    render(<FriendlyDate date="2024-01-01T00:00:00" />);
    expect(screen.getByText("January 1, 2024")).toBeInTheDocument();
  });

  it('displays friendly date format for future dates', () => {
    render(<FriendlyDate date="2024-02-22T00:00:00" />);
    expect(screen.getByText("February 22, 2024")).toBeInTheDocument();
  });

  it('renders tooltip with full text on hover', async () => {
    const {container} =  render(<FriendlyDate date="2024-01-21T00:00:00" />);
    expect(container.querySelector('span').getAttribute('data-tooltip')).toBe("January 21, 2024");
  });

  it('renders an empty span for null dates', () => {
    const {getByTestId} =  render(<FriendlyDate date={null} />);
    expect(getByTestId("friendly-date")).toBeInTheDocument();

  });

  it('renders an empty span for undefined dates', () => {
    const {getByTestId} = render(<FriendlyDate date={undefined} />);
    expect(getByTestId("friendly-date")).toBeInTheDocument();

  });

  it('renders an empty span for invalid', () => {
    const {getByTestId} = render(<FriendlyDate date="invalid-date" />);
    expect(getByTestId("friendly-date")).toBeInTheDocument();

  });

  it('renders an empty span for empty', () => {
    const {getByTestId} = render(<FriendlyDate date="" />);
    expect(getByTestId("friendly-date")).toBeInTheDocument();

  });
  
  
});
