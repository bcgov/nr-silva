import React from "react";
import { render, screen } from "@testing-library/react";
import OpeningOverview from "../../../components/OpeningDetails/OpeningOverview";
import {
  OpeningDetailsOverviewDto,
  CommentDto,
} from "../../../services/OpenApi";
import { formatLocalDate } from "../../../utils/DateUtils";

describe("OpeningOverview", () => {
  const mockComment: CommentDto = {
    commentSource: { code: "OPEN", description: "Opening" },
    commentType: { code: "GENERAL", description: "General Comments" },
    commentText: "Test comment",
  };

  const mockOverview: OpeningDetailsOverviewDto = {
    opening: {
      licenseeId: "12345",
      tenureType: { code: "A02", description: "Tree Farm Licence" },
      managementUnitType: { code: "T", description: "TREE FARM LICENCE" },
      managementUnitId: "47",
      timberSaleOffice: { code: "LSB", description: "Lumber Sale Branch" },
      comments: [mockComment],
    },
    milestones: {
      standardsUnitId: "A",
      postHarvestDeclaredDate: "2022-01-15",
      regenDeclaredDate: "2022-03-20",
      regenOffsetYears: 3,
      regenDueDate: "2025-03-20",
      freeGrowingDeclaredDate: "2022-05-10",
      freeGrowingOffsetYears: 11,
      freeGrowingDueDate: "2033-05-10",
    },
  };

  test("renders all opening information correctly", () => {
    render(<OpeningOverview overviewObj={mockOverview} />);

    // Check Opening section title
    expect(screen.getByText("Opening")).toBeInTheDocument();

    // Use data-testid attributes to find and test card items
    expect(
      screen.getByTestId("card-item-content-licensee-opening-id")
    ).toHaveTextContent("12345");
    expect(
      screen.getByTestId("card-item-content-tenure-type")
    ).toHaveTextContent("A02 - Tree Farm Licence");
    expect(
      screen.getByTestId("card-item-content-management-unit-type")
    ).toHaveTextContent("T - TREE FARM LICENCE");
    expect(
      screen.getByTestId("card-item-content-management-unit-id")
    ).toHaveTextContent("47");
    expect(
      screen.getByTestId("card-item-content-timber-sales-office")
    ).toHaveTextContent("LSB - Lumber Sale Branch");
    expect(screen.getByTestId("card-item-content-comment")).toHaveTextContent(
      "Test comment"
    );
  });

  test("renders all milestone information correctly", () => {
    render(<OpeningOverview overviewObj={mockOverview} />);

    // Check Milestone section title
    expect(screen.getByText("Milestone")).toBeInTheDocument();

    const formattedPostHarvestDate = formatLocalDate("2022-01-15", true);
    if (formattedPostHarvestDate) {
      expect(
        screen.getByTestId("card-item-content-post-harvested-declared-date")
      ).toHaveTextContent(formattedPostHarvestDate);
    }

    const formattedRegenDate = formatLocalDate("2022-03-20", true);
    if (formattedRegenDate) {
      expect(
        screen.getByTestId("card-item-content-regeneration-declared-date")
      ).toHaveTextContent(formattedRegenDate);
    }

    expect(
      screen.getByTestId("card-item-content-regeneration-offset-(years)")
    ).toHaveTextContent("3");

    const formattedRegenDueDate = formatLocalDate("2025-03-20", true);
    if (formattedRegenDueDate) {
      expect(
        screen.getByTestId("card-item-content-regeneration-due-date")
      ).toHaveTextContent(formattedRegenDueDate);
    }

    const formattedFGDate = formatLocalDate("2022-05-10", true);
    if (formattedFGDate) {
      expect(
        screen.getByTestId("card-item-content-free-growing-declared-date")
      ).toHaveTextContent(formattedFGDate);
    }

    expect(
      screen.getByTestId("card-item-content-free-growing-offset-(years)")
    ).toHaveTextContent("11");

    const formattedFGDueDate = formatLocalDate("2033-05-10", true);
    if (formattedFGDueDate) {
      expect(
        screen.getByTestId("card-item-content-free-growing-due-date")
      ).toHaveTextContent(formattedFGDueDate);
    }
  });

  test("renders skeleton when isLoading is true", () => {
    const { container } = render(<OpeningOverview isLoading={true} />);

    // Check for skeleton classes in the component
    const skeletons = container.querySelectorAll(".cds--skeleton");
    expect(skeletons.length).toBeGreaterThan(0);
  });

  test("handles null values correctly", () => {
    const emptyOverview: OpeningDetailsOverviewDto = {
      opening: {
        licenseeId: null,
        tenureType: { code: "", description: "" },
        managementUnitType: { code: "", description: "" },
        managementUnitId: null,
        timberSaleOffice: { code: "", description: "" },
        comments: [],
      },
      milestones: {
        standardsUnitId: null,
        postHarvestDeclaredDate: null,
        regenDeclaredDate: null,
        regenOffsetYears: null,
        regenDueDate: null,
        freeGrowingDeclaredDate: null,
        freeGrowingOffsetYears: null,
        freeGrowingDueDate: null,
      },
    };

    render(<OpeningOverview overviewObj={emptyOverview} />);

    // Verify that the component renders without errors when given null values
    expect(screen.getByText("Opening")).toBeInTheDocument();
    expect(screen.getByText("Milestone")).toBeInTheDocument();
  });
});
