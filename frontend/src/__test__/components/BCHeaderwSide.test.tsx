import React from "react";
import { render, screen, fireEvent, act } from "@testing-library/react";
import { describe, expect, it, vi, beforeEach, beforeAll } from "vitest";
import { MemoryRouter } from "react-router-dom";
import BCHeader from "../../components/BCHeader";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import "@testing-library/jest-dom";
import { AuthProvider } from "../../contexts/AuthProvider";
import { ThemePreference } from "../../utils/ThemePreference";
import { getMainActivitiesItems } from "../../components/BCHeader/constants";
import { PreferenceProvider } from "../../contexts/PreferenceProvider";

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
  const actual = await importOriginal<typeof import("react-router-dom")>();
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

vi.mock("../../components/BCHeader/constants", () => ({
  getMainActivitiesItems: vi.fn(() => [
    {
      name: "Main activities",
      items: [
        {
          id: "dashboard",
          name: "Dashboard",
          icon: "Dashboard",
          link: "/dashboard",
          disabled: false,
          breadcrumb: false,
        },
        {
          id: "openings",
          name: "Openings",
          icon: "MapBoundaryVegetation",
          link: "/openings",
          disabled: false,
          breadcrumb: false,
        },
        {
          id: "no-link-item",
          name: "No Link Item",
          icon: "MapBoundaryVegetation",
          link: undefined as any,
          disabled: false,
          breadcrumb: false,
        },
        {
          id: "nested-section",
          name: "Nested Section",
          icon: "CropHealth",
          link: "/nested",
          disabled: false,
          breadcrumb: false,
          subItems: [
            {
              id: "sub-1",
              name: "Sub Option 1",
              link: "/nested/sub-1",
              disabled: false,
              breadcrumb: false,
            },
            {
              id: "sub-2",
              name: "Sub Option 2",
              link: "/nested/sub-2",
              disabled: false,
              breadcrumb: false,
            },
          ],
        },
      ],
    },
  ]),
}));

vi.mock("../../services/TestService", () => ({
  getForestClientByNumberOrAcronym: vi.fn(() => [
    {
      clientNumber: "00012797",
      clientName: "MINISTRY OF FORESTS",
      legalFirstName: "",
      legalMiddleName: "",
      clientStatusCode: { code: "ACT", description: "Active" },
      clientTypeCode: {
        code: "F",
        description: "Ministry of Forests and Range",
      },
      acronym: "MOF",
    },
  ]),
}));

const renderComponent = async (initialEntries = ["/dashboard"]) => {
  const qc = new QueryClient();

  await act(async () =>
    render(
      <AuthProvider>
        <QueryClientProvider client={qc}>
          <PreferenceProvider>
            <ThemePreference>
              <MemoryRouter initialEntries={initialEntries}>
                <BCHeader />
              </MemoryRouter>
            </ThemePreference>
          </PreferenceProvider>
        </QueryClientProvider>
      </AuthProvider>
    )
  );
};

describe("BCHeader", () => {
  beforeAll(() => {
    Object.defineProperty(window, "matchMedia", {
      writable: true,
      value: vi.fn().mockImplementation((query) => ({
        matches: query === "(prefers-color-scheme: dark)",
        media: query,
        onchange: null,
        addEventListener: vi.fn(),
        removeEventListener: vi.fn(),
        dispatchEvent: vi.fn(),
      })),
    });
  });

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("should render the component", async () => {
    await renderComponent();
    expect(await screen.findByTestId("header")).toBeInTheDocument();
  });


  it("should renders the site name", async () => {
    await renderComponent();
    expect(screen.getByText("Silva")).toBeInTheDocument();
  });

  it("opens and closes the My Profile panel via profile button", async () => {
    await renderComponent();
    const userSettingsButton = screen.getByTestId("header-button__user");
    act(() => {
      fireEvent.click(userSettingsButton);
    });
    expect(screen.getByText("My Profile")).toBeInTheDocument();

    act(() => {
      fireEvent.click(userSettingsButton);
    });
  });

  it("closes the My Profile panel when clicking outside", async () => {
    await renderComponent();
    const userSettingsButton = screen.getByTestId("header-button__user");
    act(() => {
      fireEvent.click(userSettingsButton);
    });
    expect(screen.getByLabelText("User Profile Tab")).toHaveClass(
      "cds--header-panel--expanded"
    );

    act(() => {
      fireEvent.mouseDown(document.body);
    });
    expect(screen.getByLabelText("User Profile Tab")).not.toHaveClass(
      "cds--header-panel--expanded"
    );
  });

  it("closes the My Profile panel using the close button on RightPanelTitle", async () => {
    await renderComponent();
    const userSettingsButton = screen.getByTestId("header-button__user");
    act(() => {
      fireEvent.click(userSettingsButton);
    });
    expect(screen.getByLabelText("User Profile Tab")).toHaveClass(
      "cds--header-panel--expanded"
    );

    const closeButton = screen.getByRole("button", { name: "Close" });
    act(() => {
      fireEvent.click(closeButton);
    });
    expect(screen.getByLabelText("User Profile Tab")).not.toHaveClass(
      "cds--header-panel--expanded"
    );
  });

  it("renders the correct menu item names", async () => {
    await renderComponent();
    getMainActivitiesItems().forEach((item) => {
      expect(screen.getByText(item.name)).toBeInTheDocument();
    });
  });

  it("renders sub menu items and navigates on click", async () => {
    await renderComponent();
    expect(screen.getByText("Nested Section")).toBeInTheDocument();
    const subOption1 = screen.getByTestId("side-nav-item-sub-1");
    expect(subOption1).toBeInTheDocument();

    act(() => {
      fireEvent.click(subOption1);
    });
    expect(mockNavigate).toHaveBeenCalledWith("/nested/sub-1");
  });

  it("navigates when clicking a standard side nav link", async () => {
    await renderComponent();
    const openingsLink = screen.getByTestId("side-nav-link-openings");
    act(() => {
      fireEvent.click(openingsLink);
    });
    expect(mockNavigate).toHaveBeenCalledWith("/openings");
  });

  it("activates menu items correctly based on current path", async () => {
    await renderComponent(["/nested/sub-1"]);
    const subOption1 = screen.getByTestId("side-nav-item-sub-1");
    expect(subOption1).toBeInTheDocument();
  });

  it("toggles side navigation via HeaderMenuButton", async () => {
    await renderComponent();
    const menuButton = screen.getByLabelText("Open menu");
    act(() => {
      fireEvent.click(menuButton);
    });
    expect(menuButton).toBeInTheDocument();
  });
});

