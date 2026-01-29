import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi, beforeEach } from "vitest";
import { MemoryRouter } from "react-router-dom";
// Mock the Carbon icons module so `BookmarkAdd` is a predictable mock component
vi.mock("@carbon/icons-react", () => ({
  BookmarkAdd: () => <svg data-testid="mock-icon" />,
}));

import FavouriteCard from "../../components/FavouriteCard";
import type { FavouriteCardProps } from "../../components/FavouriteCard/definitions";

// Mock the useModal hook
vi.mock("../../contexts/ModalContext", () => ({
  useModal: () => ({
    openModal: vi.fn(),
  }),
}));

describe("FavouriteCard", () => {
  const mockIcon = () => <svg data-testid="mock-icon" />;
  const props: FavouriteCardProps = {
    index: 0,
    title: "Test Title",
    link: "/test-link",
    icon: "BookmarkAdd",
  };

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
