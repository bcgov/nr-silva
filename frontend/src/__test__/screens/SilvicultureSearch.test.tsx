import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import SilvicultureSearch from "../../screens/SilvicultureSearch";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import API from "../../services/API";
import { PreferenceProvider } from "@/contexts/PreferenceProvider";
import { PagedModelOpeningSearchResponseDto } from "../../services/OpenApi";

vi.mock("../../services/API", () => {
  return {
    default: {
      CodesEndpointService: {
        getOpeningOrgUnits: vi.fn().mockResolvedValue([]),
        getOpeningCategories: vi.fn().mockResolvedValue([]),
      },
      OpeningEndpointService: {
        openingSearch: vi.fn().mockResolvedValue({
          content: [],
          page: {
            totalElements: 0,
            size: 10,
            page: 0,
            totalPages: 1,
          },
        } as PagedModelOpeningSearchResponseDto),
      },
    },
  };
});

describe("SilvicultureSearch Component", () => {
  beforeEach(() => {
    vi.resetAllMocks();
    (API.CodesEndpointService.getOpeningOrgUnits as vi.Mock).mockResolvedValue(
      []
    );
    (
      API.CodesEndpointService.getOpeningCategories as vi.Mock
    ).mockResolvedValue([]);
    (API.OpeningEndpointService.openingSearch as vi.Mock).mockResolvedValueOnce(
      {
        content: [],
        page: {
          totalElements: 0,
          size: 10,
          page: 0,
          totalPages: 1,
        },
      } as PagedModelOpeningSearchResponseDto
    );
  });
  const queryClient = new QueryClient();

  const renderComponent = () =>
    render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <PreferenceProvider>
            <SilvicultureSearch />
          </PreferenceProvider>
        </QueryClientProvider>
      </BrowserRouter>
    );

  it("should render the page title correctly", () => {
    renderComponent();

    const titleElement = screen.getByText("Silviculture Search");
    const subtitleElement = screen.getByText(
      "Search for opening types, activities, stocking standards or standards units"
    );

    expect(titleElement).toBeInTheDocument();
    expect(subtitleElement).toBeInTheDocument();
  });
});
