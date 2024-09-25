import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import BCGovLogo from '../../components/BCGovLogo';

describe('BCGovLogo Component', () => {
  it('should render the image with correct src and alt text', () => {
    render(<BCGovLogo />);
    const logoElement = screen.getByAltText('BCGov Logo');
    expect(logoElement).toBeDefined();

    const displayedImage = document.querySelector("img") as HTMLImageElement;
    expect(displayedImage.src).toContain('bc-gov-logo.png');
  });

  it('should render the image with the correct width and class', () => {
    render(<BCGovLogo />);
    const logoElement = screen.getByAltText('BCGov Logo');
    expect(logoElement.classList).toContain('logo');

    const displayedImage = document.querySelector("img") as HTMLImageElement;
    expect(displayedImage.width).toEqual(160);
  });
});
