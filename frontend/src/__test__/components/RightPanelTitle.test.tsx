import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import RightPanelTitle from '@/components/RightPanelTitle';
import '@testing-library/jest-dom';

describe('RightPanelTitle', () => {
  it('renders title', () => {
    const mockCloseFn = vi.fn();
    render(<RightPanelTitle title="Test Title" closeFn={mockCloseFn} />);
    expect(screen.getByText('Test Title')).toBeInTheDocument();
  });

  it('renders h4 element', () => {
    const mockCloseFn = vi.fn();
    const { container } = render(<RightPanelTitle title="Title" closeFn={mockCloseFn} />);
    expect(container.querySelector('h4')).toBeInTheDocument();
  });

  it('renders close button with ghost kind', () => {
    const mockCloseFn = vi.fn();
    render(<RightPanelTitle title="Title" closeFn={mockCloseFn} />);
    const closeButton = screen.getByRole('button', { name: /close/i });
    expect(closeButton).toBeInTheDocument();
  });

  it('calls closeFn when close button is clicked', async () => {
    const user = userEvent.setup();
    const mockCloseFn = vi.fn();
    render(<RightPanelTitle title="Title" closeFn={mockCloseFn} />);

    const closeButton = screen.getByRole('button', { name: /close/i });
    await user.click(closeButton);

    expect(mockCloseFn).toHaveBeenCalledTimes(1);
  });

  it('renders container div with right-title-section class', () => {
    const mockCloseFn = vi.fn();
    const { container } = render(<RightPanelTitle title="Title" closeFn={mockCloseFn} />);
    expect(container.querySelector('.right-title-section')).toBeInTheDocument();
  });

  it('renders title inside h4', () => {
    const mockCloseFn = vi.fn();
    const { container } = render(<RightPanelTitle title="My Title" closeFn={mockCloseFn} />);
    const h4 = container.querySelector('h4');
    expect(h4?.textContent).toBe('My Title');
  });

  it('renders long title', () => {
    const mockCloseFn = vi.fn();
    const longTitle = 'This is a very long title that might wrap';
    render(<RightPanelTitle title={longTitle} closeFn={mockCloseFn} />);
    expect(screen.getByText(longTitle)).toBeInTheDocument();
  });

  it('renders div with right-title-buttons class', () => {
    const mockCloseFn = vi.fn();
    const { container } = render(<RightPanelTitle title="Title" closeFn={mockCloseFn} />);
    expect(container.querySelector('.right-title-buttons')).toBeInTheDocument();
  });

  it('does not call closeFn initially', () => {
    const mockCloseFn = vi.fn();
    render(<RightPanelTitle title="Title" closeFn={mockCloseFn} />);
    expect(mockCloseFn).not.toHaveBeenCalled();
  });

  it('calls closeFn once per click', async () => {
    const user = userEvent.setup();
    const mockCloseFn = vi.fn();
    render(<RightPanelTitle title="Title" closeFn={mockCloseFn} />);

    const closeButton = screen.getByRole('button', { name: /close/i });
    await user.click(closeButton);
    await user.click(closeButton);

    expect(mockCloseFn).toHaveBeenCalledTimes(2);
  });

  it('renders with special characters in title', () => {
    const mockCloseFn = vi.fn();
    render(<RightPanelTitle title="Title @#$%" closeFn={mockCloseFn} />);
    expect(screen.getByText('Title @#$%')).toBeInTheDocument();
  });

  it('renders h4 as direct child of main div', () => {
    const mockCloseFn = vi.fn();
    const { container } = render(<RightPanelTitle title="Title" closeFn={mockCloseFn} />);
    const section = container.querySelector('.right-title-section');
    const h4 = section?.querySelector('h4');
    expect(h4?.parentElement).toBe(section);
  });

  it('renders buttons div as direct child of main div', () => {
    const mockCloseFn = vi.fn();
    const { container } = render(<RightPanelTitle title="Title" closeFn={mockCloseFn} />);
    const section = container.querySelector('.right-title-section');
    const buttons = section?.querySelector('.right-title-buttons');
    expect(buttons?.parentElement).toBe(section);
  });
});
