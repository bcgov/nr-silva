import React from "react";
import { MemoryRouter } from 'react-router-dom';
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import ActionButtons from "../../components/ActionButtons";
import { NotificationProvider } from "../../contexts/NotificationProvider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

describe("ActionButtons", () => {
  const rowId = "123456";
  const favorited = false;
  const queryClient = new QueryClient();

  const renderWithProviders = () => {
    return render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <NotificationProvider>
            <ActionButtons favorited={favorited} rowId={rowId} />
          </NotificationProvider>
        </MemoryRouter>
      </QueryClientProvider>
    );
  };

  it("renders the 'Favorite Opening' button", () => {
    renderWithProviders();
    expect(screen.getByRole("button", { name: /Favorite Opening/i })).toBeInTheDocument();
  });


  it("sets the 'Favorite Opening' as favorited when button is clicked", async () => {
    renderWithProviders();

    // Find the button
    const favButton = screen.getByRole("button", { name: /Favorite Opening/i });

    // Ensure initial state is "unfavorited"
    expect(favButton).toHaveAttribute("aria-pressed", "false");

    // Click the button
    fireEvent.click(favButton);

    // Wait for the button to update
    await waitFor(() => {
      expect(favButton).toHaveAttribute("aria-pressed", "true");
    });
  });
});
