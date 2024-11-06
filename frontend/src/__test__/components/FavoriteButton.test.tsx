import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import FavoriteButton from '../../components/FavoriteButton';
import '@testing-library/jest-dom';
import { on } from 'events';

describe('FavoriteButton Component', () => {
  const props = {
    tooltipPosition: 'bottom',
    kind: 'ghost',
    size: 'md',
    fill: 'red',
    favorited: false,
    onFavoriteChange: vi.fn(),
  };

  it('should render the component with default state', () => {
    render(<FavoriteButton {...props} />);
    const buttonElement = screen.getByRole('button');
    expect(buttonElement).toBeDefined();
    expect(buttonElement.classList).toContain('favorite-button'); 
  });

  it('should toggle favorite state on click', () => {
    render(<FavoriteButton {...props} />);
    const buttonElement = screen.getByRole('button');
    fireEvent.click(buttonElement);
    expect(buttonElement.classList).toContain('favorite');
    fireEvent.click(buttonElement);
    expect(buttonElement.classList).toContain('favorite-button');
  });

  it('should render the correct icon based on favorite state', () => {
    render(<FavoriteButton {...props} />);
    const imgElement = screen.getByTestId('favourite-button-icon');
    expect(imgElement).toHaveStyle('fill: red'); 
  });

  it('should call onFavoriteChange with the new favorite state', () => {
    render(<FavoriteButton {...props} />);
    const buttonElement = screen.getByRole('button');
    fireEvent.click(buttonElement);
    expect(props.onFavoriteChange).toHaveBeenCalledWith(false);
  });
});

