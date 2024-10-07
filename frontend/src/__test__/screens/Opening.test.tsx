import React from 'react';
import { describe, expect, it, vi } from 'vitest';
import { render, waitFor } from '@testing-library/react';
import Opening from '../../screens/Opening';
import { BrowserRouter } from 'react-router-dom';
import * as redux from 'react-redux'

const state = {
  userDetails: {
    id: 1,
    name: 'User'
  }
};

vi.spyOn(redux, 'useSelector')
  .mockImplementation((callback) => callback(state));

describe('Opening screen test cases', () => {
  it('should renders Opening Page Title component', async () => {
    const { getByTestId } = render(
      <BrowserRouter>
        <Opening />
      </BrowserRouter>
    );

    const pageTitleComp = await waitFor(() => getByTestId('opening-pagetitle'));
    expect(pageTitleComp).toBeDefined();

    //const subtitle = 'Create, manage or check opening information';
    //expect(screen.getByText(subtitle)).toBeDefined();
  });
});
