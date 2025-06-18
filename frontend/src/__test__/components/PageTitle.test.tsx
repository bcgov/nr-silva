import React from "react";
import { render, screen } from "@testing-library/react";
import PageTitle from "../../components/PageTitle";
import { describe, it, expect } from "vitest";
import { MemoryRouter } from "react-router-dom";

describe("PageTitle Component", () => {
  const mockBreadCrumbs = [
    { name: "Dashboard", path: "/" },
    { name: "Opening", path: "/opening" },
    { name: "Standard Units", path: "/standard-units" },
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
    const standardUnitsCrumb = screen.getByText(/Standard Units/i);

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
