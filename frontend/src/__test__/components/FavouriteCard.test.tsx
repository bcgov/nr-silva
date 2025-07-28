import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi, beforeEach } from "vitest";
import { MemoryRouter } from "react-router-dom";
import * as Icons from "@carbon/icons-react";
import FavouriteCard from "../../components/FavouriteCard";

// Mock the useModal hook
vi.mock("../../contexts/ModalContext", () => ({
  useModal: () => ({
    openModal: vi.fn(),
  }),
}));

describe("FavouriteCard", () => {
  const mockIcon = () => <svg data-testid="mock-icon" />;
  const props = {
    index: 0,
    title: "Test Title",
    link: "/test-link",
    icon: "Search20" as keyof typeof Icons,
  };

  beforeEach(() => {
    (Icons as any)["Search20"] = mockIcon; // Override the icon with a mock
  });

  it("renders the card with correct content", () => {
    render(
      <MemoryRouter>
        <FavouriteCard {...props} />
      </MemoryRouter>
    );

    expect(screen.getByTestId("mock-icon")).toBeInTheDocument();
    expect(screen.getAllByText("Test Title")).toHaveLength(1);
  });
});
