import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import Reports from '../../screens/Reports';

describe('Reports', () => {
  it('should render the reports page title', () => {
    render(<Reports />);
    const titleElement = screen.getByText(/Reports Page/i);
    expect(titleElement).toBeDefined();
  });

  it('should render the form sample title', () => {
    render(<Reports />);
    const formTitleElement = screen.getByText(/Form Sample/i);
    expect(formTitleElement).toBeDefined();
  });

  it('should render the date picker component', () => {
    render(<Reports />);
    const datePickerElement = screen.getByLabelText(/Start date/i);
    expect(datePickerElement).toBeDefined();
  });

  it('should renders the dropdown component', () => {
    render(<Reports />);
    const dropdownElements = screen.getAllByLabelText(/Select Fruit from Dropdown/i);
    expect(dropdownElements.length).toBe(8);
  });

  it('renders the modal button', () => {
    render(<Reports />);
    const modalButtonElement = screen.getByText(/Launch modal/i);
    expect(modalButtonElement).toBeDefined();
  });

  it('renders the table headers', () => {
    render(<Reports />);
    const tableHeaders = ['Name', 'Rule', 'Status', 'Other', 'Example'];
    tableHeaders.forEach((header) => {
      const headerElement = screen.getByText(header);
      expect(headerElement).toBeDefined();
    });
  });

  it('renders the table rows and cells', () => {
    render(<Reports />);
    const tableRows = screen.getAllByRole('row');
    // Excluding the header row
    expect(tableRows.length).toBe(8); 

    // Example: Check for specific cell content
    const cellContent = screen.getByText('Load Balancer 1');
    expect(cellContent).toBeDefined();
  });
});
