import React from "react";
import { render, screen } from "@testing-library/react";
import PageTitle from "../../components/PageTitle";
import { describe, it, expect } from "vitest";
import { MemoryRouter } from "react-router-dom";

describe("PageTitle Component", () => {
  const mockBreadCrumbs = [
    { name: "Dashboard", path: "/" },
    { name: "Opening", path: "/opening" },
    { name: "Standards Units", path: "/standards-units" },
  ];

  it("renders the title correctly", () => {
    render(
      <MemoryRouter>
        <PageTitle title="Test Title" subtitle="Not what you expected" />
      </MemoryRouter>
    );
    const titleElement = screen.getByText(/Test Title/i);
    expect(titleElement).toBeInTheDocument();
  });

  it('renders a React-node subtitle without wrapping it as plain text', () => {
    render(
      <MemoryRouter>
        <PageTitle title="Test Title" subtitle={<span data-testid="opening-tag">Opening ID #123</span>} />
      </MemoryRouter>
    );

    expect(screen.getByTestId('opening-tag')).toHaveTextContent('Opening ID #123');
  });

  it("renders the breadcrumb correctly", () => {
    render(
      <MemoryRouter>
        <PageTitle
          title="Test Title"
          subtitle="Not what you expected"
          breadCrumbs={mockBreadCrumbs}
        />
      </MemoryRouter>
    );

    const openingCrumb = screen.getByText(/Opening/i);
    const standardUnitsCrumb = screen.getByText(/Standards Units/i);

    expect(openingCrumb).toBeInTheDocument();
    expect(standardUnitsCrumb).toBeInTheDocument();
  });

  it("renders empty breadcrumb when breadCrumbs prop is an empty array", () => {
    render(
      <MemoryRouter>
        <PageTitle
          title="Test Title"
          subtitle="Not what you expected"
          breadCrumbs={[]}
        />
      </MemoryRouter>
    );

    const olElement = screen.queryByRole("list");
    expect(olElement).not.toBeInTheDocument();
  });

  it("does not render breadcrumbs when breadCrumbs prop is not provided", () => {
    render(
      <MemoryRouter>
        <PageTitle
          title="Internal Affairs"
          subtitle="Your secret, our secret"
        />
      </MemoryRouter>
    );

    const olElement = screen.queryByRole("list");
    expect(olElement).not.toBeInTheDocument();
  });
});
