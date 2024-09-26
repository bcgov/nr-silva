import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import ChartTitle from '../../components/ChartTitle';

const title = 'Test Title';
const subtitle = 'Test Subtitle';

describe('ChartTitle Component', () => {
  it('should render the chart title with title and subtitle', () => {
    render(<ChartTitle title={title} subtitle={subtitle} />);
    expect(screen.getByText(title)).toBeDefined();
    expect(screen.getByText(subtitle)).toBeDefined();
  });
});
