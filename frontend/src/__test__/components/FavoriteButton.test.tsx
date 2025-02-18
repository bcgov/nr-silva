import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import FavoriteButton from '../../components/FavoriteButton';
import '@testing-library/jest-dom';

import { FavoriteButtonProps } from '../../components/FavoriteButton/definitions';

describe('FavoriteButton Component', () => {
  const props: FavoriteButtonProps = {
    tooltipPosition: 'bottom',
    kind: 'ghost',
    size: 'md',
    favorited: false,
    onFavoriteChange: vi.fn(),
  };

  it('should render the component with default state', () => {
    render(<FavoriteButton {...props} />);
    const buttonElement = screen.getByRole('button');
    expect(buttonElement).toBeInTheDocument();
    expect(buttonElement).toHaveClass('favorite-button');
    expect(buttonElement).toHaveAttribute('aria-pressed', 'false');
  });

  it('should toggle favorite state on click', () => {
    render(<FavoriteButton {...props} />);
    const buttonElement = screen.getByRole('button');

    // Initial state
    expect(buttonElement).toHaveAttribute('aria-pressed', 'false');

    // Click to favorite
    fireEvent.click(buttonElement);
    expect(buttonElement).toHaveAttribute('aria-pressed', 'true');

    // Click again to unfavorite
    fireEvent.click(buttonElement);
    expect(buttonElement).toHaveAttribute('aria-pressed', 'false');
  });

  it('should call onFavoriteChange with the correct favorite state', () => {
    render(<FavoriteButton {...props} />);
    const buttonElement = screen.getByRole('button');

    fireEvent.click(buttonElement);
    expect(props.onFavoriteChange).toHaveBeenCalledWith(true);

    fireEvent.click(buttonElement);
    expect(props.onFavoriteChange).toHaveBeenCalledWith(false);
  });
});
