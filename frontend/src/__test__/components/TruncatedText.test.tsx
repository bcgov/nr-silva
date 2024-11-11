import React from 'react';
import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import TruncatedText from '../../components/TruncatedText';

// Mock Tooltip component from Carbon to ensure tests run without extra dependencies
vi.mock('@carbon/react', () => {
  const Tooltip = ({ label, children }) => <span data-tooltip={label}>{children}</span>;
  return { Tooltip };
});

describe('TruncatedText Component', () => {

  it('renders the component with default props', () => {
    render(<TruncatedText text="This is a test text that may be truncated" />);
    expect(screen.getByText(/This is a test text/)).toBeInTheDocument();
  });

  it('truncates text based on maxLength', () => {
    render(<TruncatedText text="This is a test text that should be truncated" maxLength={10} />);
    expect(screen.getByText("This is a ...")).toBeInTheDocument();
  });

  it('does not truncate text if length is within maxLength', () => {
    render(<TruncatedText text="Short text" maxLength={20} />);
    expect(screen.getByText("Short text")).toBeInTheDocument();
  });

  it('truncates text based on parentWidth less than 200px', () => {
    render(<TruncatedText text="This is a test text that should be truncated" parentWidth={150} />);
    expect(screen.getByText("This is a test ...")).toBeInTheDocument(); // 150/10 = 15 chars
  });

  it('truncates text based on parentWidth greater than 200px', () => {
    const text = "This is a test text that should be truncated".repeat(10);
    render(<TruncatedText text={text} parentWidth={300} />);
    expect(screen.getByText("This is a test text that should be truncatedThis is a test t...")).toBeInTheDocument(); // 300/5 = 60 chars
  });

  it('renders tooltip with full text on hover', async () => {
    const {container} = render(<TruncatedText text="This is a test text that should be truncated" maxLength={10} />);
    expect(container.querySelector('span').getAttribute('data-tooltip')).toBe("This is a test text that should be truncated");
  });

});
