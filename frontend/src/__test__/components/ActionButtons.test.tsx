// ActionButtons.test.tsx
import React from "react";
import { MemoryRouter } from 'react-router-dom';
import { render, screen, fireEvent } from "@testing-library/react";
import ActionButtons from "../../components/ActionButtons";
import { NotificationProvider } from "../../contexts/NotificationProvider"

describe("ActionButtons", () => {
  const rowId = "123456";
  const favorited = false;

  it("renders the 'Favorite Opening' and 'Document Download' buttons", () => {
    render(
      <MemoryRouter>
        <NotificationProvider>
          <ActionButtons favorited={favorited} rowId={rowId} />
        </NotificationProvider>
      </MemoryRouter>
    );

    // Check that both buttons are in the document
    expect(screen.getByRole("button", { name: /Favorite Opening/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Document Download/i })).toBeInTheDocument();
  });

  it("set the 'Favorite Opening' as favorited when button is clicked", () => {
    render(
      <MemoryRouter>
        <NotificationProvider>
          <ActionButtons favorited={favorited} rowId={rowId} />
        </NotificationProvider>
      </MemoryRouter>
    );

    // Find the "View" button and click it
    const viewButton = screen.getByRole("button", { name: /Favorite Opening/i });
    expect(viewButton.classList).toContain('favorite-button');
    fireEvent.click(viewButton);
    expect(screen.getByRole("button", { name: /Favorite Opening/i }).classList).toContain('favorite');
  });
});
