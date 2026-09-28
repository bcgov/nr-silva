import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import CustomMultiSelect from '@/components/CustomMultiSelect';

describe('CustomMultiSelect', () => {
  const items = [
    { id: '1', label: 'Item 1' },
    { id: '2', label: 'Item 2' },
    { id: '3', label: 'Item 3' },
  ];

  it('renders skeleton loader when showSkeleton is true', () => {
    const { container } = render(
      <CustomMultiSelect
        id="custom-select"
        items={items}
        itemToString={(item) => item?.label ?? ''}
        showSkeleton={true}
      />
    );

    // TextInputSkeleton has cds--skeleton class
    expect(container.querySelector('.cds--skeleton')).toBeInTheDocument();
  });

  it('renders FilterableMultiSelect when showSkeleton is false', () => {
    render(
      <CustomMultiSelect
        id="custom-select"
        titleText="Select items"
        items={items}
        itemToString={(item) => item?.label ?? ''}
      />
    );

    expect(screen.getByText('Select items')).toBeInTheDocument();
  });

  it('defers onChange call via queueMicrotask', async () => {
    const mockOnChange = vi.fn();
    render(
      <CustomMultiSelect
        id="custom-select"
        titleText="Select items"
        items={items}
        itemToString={(item) => item?.label ?? ''}
        onChange={mockOnChange}
      />
    );

    const input = screen.getByRole('combobox');
    fireEvent.click(input);

    const option = await screen.findByText('Item 1');
    fireEvent.click(option);

    await waitFor(() => {
      expect(mockOnChange).toHaveBeenCalled();
    });
  });

  it('blurs multiselect container and input when user clicks outside', () => {
    const blurSpy = vi.spyOn(HTMLElement.prototype, 'blur');

    render(
      <div>
        <div data-testid="outside-element">Outside</div>
        <CustomMultiSelect
          id="custom-select"
          titleText="Select items"
          items={items}
          itemToString={(item) => item?.label ?? ''}
        />
      </div>
    );

    const outside = screen.getByTestId('outside-element');
    blurSpy.mockClear();

    fireEvent.mouseDown(outside);

    expect(blurSpy).toHaveBeenCalled();
    blurSpy.mockRestore();
  });

  it('does not blur when click is inside the multiselect', () => {
    const { container } = render(
      <CustomMultiSelect
        id="custom-select"
        titleText="Select items"
        items={items}
        itemToString={(item) => item?.label ?? ''}
      />
    );

    const multiSelect = container.querySelector(
      '.cds--multi-select, .bx--multi-select'
    ) as HTMLElement;
    expect(multiSelect).not.toBeNull();
    const blurSpy = vi.spyOn(multiSelect, 'blur');
    fireEvent.mouseDown(multiSelect);
    expect(blurSpy).not.toHaveBeenCalled();
    blurSpy.mockRestore();
  });

  it('removes document event listener on unmount', () => {
    const removeEventListenerSpy = vi.spyOn(document, 'removeEventListener');
    const { unmount } = render(
      <CustomMultiSelect
        id="custom-select"
        titleText="Select items"
        items={items}
        itemToString={(item) => item?.label ?? ''}
      />
    );

    unmount();
    expect(removeEventListenerSpy).toHaveBeenCalledWith('mousedown', expect.any(Function), true);
  });
});
