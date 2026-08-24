import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import Avatar from '@/components/Avatar';
import '@testing-library/jest-dom';

describe('Avatar', () => {
  it('renders avatar with initial', () => {
    render(<Avatar initial="J" size="sm" />);
    expect(screen.getByText('J')).toBeInTheDocument();
  });

  it('applies size class sm', () => {
    const { container } = render(<Avatar initial="A" size="sm" />);
    expect(container.querySelector('.initial-avatar-sm')).toBeInTheDocument();
  });

  it('applies size class md', () => {
    const { container } = render(<Avatar initial="B" size="md" />);
    expect(container.querySelector('.initial-avatar-md')).toBeInTheDocument();
  });

  it('applies background color class for initial letter', () => {
    const { container } = render(<Avatar initial="C" size="sm" />);
    const avatar = container.querySelector('.initial-avatar-sm');
    expect(avatar).toHaveClass('initial-bg-c');
  });

  it('applies lowercase color class for uppercase initial', () => {
    const { container } = render(<Avatar initial="Z" size="sm" />);
    const avatar = container.querySelector('.initial-avatar-sm');
    expect(avatar).toHaveClass('initial-bg-z');
  });

  it('does not apply color class for empty initial', () => {
    const { container } = render(<Avatar initial="" size="sm" />);
    const avatar = container.querySelector('.initial-avatar-sm');
    expect(avatar).not.toHaveClass(/initial-bg-/);
  });

  it('renders single character initial', () => {
    render(<Avatar initial="X" size="md" />);
    expect(screen.getByText('X')).toBeInTheDocument();
  });

  it('renders first character only when multiple characters provided', () => {
    render(<Avatar initial="AB" size="sm" />);
    const element = screen.getByText('AB');
    expect(element).toHaveClass('initial-bg-a');
  });

  it('combines size and color classes', () => {
    const { container } = render(<Avatar initial="M" size="md" />);
    const avatar = container.querySelector('div');
    expect(avatar?.className).toMatch(/initial-avatar-md/);
    expect(avatar?.className).toMatch(/initial-bg-m/);
  });

  it('renders with different sizes', () => {
    const { rerender, container } = render(<Avatar initial="T" size="sm" />);
    expect(container.querySelector('.initial-avatar-sm')).toBeInTheDocument();

    rerender(<Avatar initial="T" size="md" />);
    expect(container.querySelector('.initial-avatar-md')).toBeInTheDocument();
  });

  it('handles numeric initial', () => {
    render(<Avatar initial="1" size="sm" />);
    expect(screen.getByText('1')).toBeInTheDocument();
  });

  it('handles special character initial', () => {
    render(<Avatar initial="@" size="md" />);
    expect(screen.getByText('@')).toBeInTheDocument();
  });

  it('applies both size class and bg class together', () => {
    const { container } = render(<Avatar initial="K" size="md" />);
    const element = container.querySelector('div');
    expect(element).toHaveClass('initial-avatar-md');
    expect(element).toHaveClass('initial-bg-k');
  });

  it('renders with sm size and applies correct classes', () => {
    const { container } = render(<Avatar initial="S" size="sm" />);
    const div = container.querySelector('div');
    expect(div).toHaveClass('initial-avatar-sm', 'initial-bg-s');
  });
});
