import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { vi, describe, it, expect, beforeEach } from "vitest";
import AutocompleteClientLocation from "../../components/AutocompleteClientLocation";
import { useAutocomplete } from "../../contexts/AutocompleteProvider";

// Mock the `useAutocomplete` hook
vi.mock("../../contexts/AutocompleteProvider", () => ({
  useAutocomplete: vi.fn(),
}));

// Mock API data
const mockClients = [
  { id: "1", label: "Client One, 1, C1" },
  { id: "2", label: "Client Two, 2, C2" },
];

const mockLocations = [
  { id: "A", label: "A - Location A" },
  { id: "B", label: "B - Location B" },
];

describe("AutocompleteClientLocation", () => {
  const mockFetchOptions = vi.fn();
  const mockUpdateOptions = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useAutocomplete as any).mockReturnValue({
      options: {
        clients: mockClients,
        locations: mockLocations,
      },
      fetchOptions: mockFetchOptions,
      updateOptions: mockUpdateOptions,
    });
  });

  it("renders both ComboBoxes and their titles", () => {
    render(<AutocompleteClientLocation setLocationValue={vi.fn()} setClientValue={vi.fn()}/>);
    expect(screen.getByText("Client")).toBeInTheDocument();
    expect(screen.getByText("Location code")).toBeInTheDocument();
  });

  it("disables the Location ComboBox initially", () => {
    render(<AutocompleteClientLocation setLocationValue={vi.fn()} setClientValue={vi.fn()} />);
    expect(screen.getByRole("combobox", { name: /location code/i })).toBeDisabled();
  });

  it("calls fetchOptions when typing in the Client ComboBox", async () => {
    render(<AutocompleteClientLocation setLocationValue={vi.fn()} setClientValue={vi.fn()} />);

    const clientInput = screen.getByRole("combobox", { name: /client/i });
    await userEvent.type(clientInput, "Client");

    await waitFor(() => {
      expect(mockFetchOptions).toHaveBeenCalledWith("Client", "clients");
    });
  });

  it("enables the Location ComboBox when a client is selected", async () => {
    render(<AutocompleteClientLocation setLocationValue={vi.fn()} setClientValue={vi.fn()} />);

    const clientInput = screen.getByRole("combobox", { name: /client/i });
    await userEvent.type(clientInput, "Client");

    const clientOption = screen.getByText(mockClients[0].label);
    await userEvent.click(clientOption);

    expect(screen.getByRole("combobox", { name: /location code/i })).not.toBeDisabled();
    expect(mockFetchOptions).toHaveBeenCalledWith("1", "locations");
  });

  it("clears the location selection when the client is reset", async () => {
    const mockSetLocationValue = vi.fn();
    const mockSetClientValue = vi.fn();
    
    render(<AutocompleteClientLocation setLocationValue={mockSetLocationValue} setClientValue={mockSetClientValue} />);

    // Select a client
    const clientInput = screen.getByRole("combobox", { name: /client/i });
    await userEvent.type(clientInput, "Client");
    const clientOption = screen.getByText(mockClients[0].label);
    await userEvent.click(clientOption);
    expect(mockFetchOptions).toHaveBeenCalledWith("1", "locations");

    // Select a location
    const locationInput = screen.getByRole("combobox", { name: /location code/i });
    await userEvent.click(locationInput);
    const locationOption = screen.getByText(mockLocations[0].label);
    await userEvent.click(locationOption);
    expect(mockSetLocationValue).toHaveBeenCalledWith("A");

    // Clear client selection
    const clientClearButton = screen.getAllByRole("button", { name: /clear/i });
    fireEvent.click(clientClearButton[0]);

    expect(mockUpdateOptions).toHaveBeenCalledWith("locations", []);
    expect(mockUpdateOptions).toHaveBeenCalledWith("clients", []);
    expect(mockSetLocationValue).toHaveBeenCalledWith(null);
  });

  it("calls setValue when a location is selected", async () => {
    const mockSetLocationValue = vi.fn();
    const mockSetClientValue = vi.fn();
    
    render(<AutocompleteClientLocation setLocationValue={mockSetLocationValue} setClientValue={mockSetClientValue} />);

    // Select a client
    const clientInput = screen.getByRole("combobox", { name: /client/i });
    await userEvent.type(clientInput, "Client");
    const clientOption = screen.getByText(mockClients[0].label);
    await userEvent.click(clientOption);
    expect(mockFetchOptions).toHaveBeenCalledWith("1", "locations");

    // Select a location
    const locationInput = screen.getByRole("combobox", { name: /location code/i });
    await userEvent.click(locationInput);
    const locationOption = screen.getByText(mockLocations[0].label);
    await userEvent.click(locationOption);
    expect(mockSetLocationValue).toHaveBeenCalledWith("A");
  });
});
