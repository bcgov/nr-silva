// ActionButtons.test.tsx
import React from "react";
import { vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import ActionButtons from "../../components/ActionButtons";

// Mock console.log
const consoleLogMock = vi.spyOn(console, "log").mockImplementationOnce(() =>vi.fn()) ;

afterEach(() => {
  consoleLogMock.mockClear();
});

afterAll(() => {
  consoleLogMock.mockRestore();
});

describe("ActionButtons", () => {
  const rowId = "test-row-id";

  it("renders the 'View' and 'Document Download' buttons", () => {
    render(<ActionButtons rowId={rowId} />);

    // Check that both buttons are in the document
    expect(screen.getByRole("button", { name: /View/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Document Download/i })).toBeInTheDocument();
  });

  it("calls console.log with rowId when the 'View' button is clicked", () => {
    render(<ActionButtons rowId={rowId} />);

    // Find the "View" button and click it
    const viewButton = screen.getByRole("button", { name: /View/i });
    fireEvent.click(viewButton);

    // Check if console.log was called with the correct rowId
    expect(consoleLogMock).toHaveBeenCalledWith(rowId);
  });
});
