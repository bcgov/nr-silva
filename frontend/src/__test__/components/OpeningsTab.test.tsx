import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import OpeningsTab from '../../components/OpeningsTab';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux'
import store from '../../store'

Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation(query => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(), // Deprecated
    removeListener: vi.fn(), // Deprecated
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
});

describe('OpeningsTab component basic tests', () => {
  it('renders without crashing', () => {
    const { getByText } = render(
      <Provider store={store}>
        <BrowserRouter>
          <OpeningsTab showSpatial={false} setShowSpatial={vi.fn()} />
        </BrowserRouter>
      </Provider>
    );

    // Check if the title renders correctly
    const titleElement = getByText('Recent openings');
    expect(titleElement).not.toBeNull(); // Ensure title element is found
    expect(titleElement.textContent).toBe('Recent openings'); // Check the text content
  });

  it('should render Show Spatial button when showSpatial is false', () => {
    const { getByText } = render(
      <Provider store={store}>
        <BrowserRouter>
          <OpeningsTab showSpatial={false} setShowSpatial={vi.fn()} />
        </BrowserRouter>
      </Provider>
    );

    const button = getByText('Show Spatial');
    expect(button).not.toBeNull(); // Ensure the Show Spatial button is rendered
  });

  it('should render Hide Spatial button when showSpatial is true', () => {
    const { getByText } = render(
      <Provider store={store}>
        <BrowserRouter>
          <OpeningsTab showSpatial={true} setShowSpatial={vi.fn()} />
        </BrowserRouter>
      </Provider>
    );

    const button = getByText('Hide Spatial');
    expect(button).not.toBeNull(); // Ensure the Hide Spatial button is rendered
  });
});

describe('OpeningsTab component tests for button click', () => {
    it('should call setShowSpatial when the button is clicked', () => {
      const setShowSpatialMock = vi.fn();
  
      const { getByText } = render(
        <Provider store={store}>
          <BrowserRouter>
            <OpeningsTab showSpatial={false} setShowSpatial={setShowSpatialMock} />
          </BrowserRouter>
        </Provider>
      );
  
      // Find the button and simulate a click
      const button = getByText('Show Spatial');
      fireEvent.click(button);
  
      // Verify that setShowSpatial was called (once in this case)
      expect(setShowSpatialMock).toHaveBeenCalledTimes(1);
      expect(setShowSpatialMock).toHaveBeenCalledWith(expect.any(Function)); // It should be called with a function
    });
  
    it('should call setShowSpatial and toggle to false when showSpatial is true', () => {
      const setShowSpatialMock = vi.fn();
  
      const { getByText } = render(
        <Provider store={store}>
          <BrowserRouter>
            <OpeningsTab showSpatial={true} setShowSpatial={setShowSpatialMock} />
          </BrowserRouter>
        </Provider>
      );
  
      // Find the button and simulate a click
      const button = getByText('Hide Spatial');
      fireEvent.click(button);
  
      // Verify that setShowSpatial was called (once in this case)
      expect(setShowSpatialMock).toHaveBeenCalledTimes(1);
      expect(setShowSpatialMock).toHaveBeenCalledWith(expect.any(Function)); // Again, should be called with a function
    });
  });
