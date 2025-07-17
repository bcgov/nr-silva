import React from "react";
import { render, screen, fireEvent, act } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { BrowserRouter } from "react-router-dom";
import BCHeader from "../../components/BCHeader";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import "@testing-library/jest-dom";
import { AuthProvider } from "../../contexts/AuthProvider";
import { ThemePreference } from "../../utils/ThemePreference";
import { mainActivitiesItems } from "../../components/BCHeader/constants";
import { PreferenceProvider } from "../../contexts/PreferenceProvider";

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

const renderComponent = async () => {
  const qc = new QueryClient();

  await act(async () =>
    render(
      <AuthProvider>
        <QueryClientProvider client={qc}>
          <PreferenceProvider>
            <ThemePreference>
              <BrowserRouter>
                <BCHeader />
              </BrowserRouter>
            </ThemePreference>
          </PreferenceProvider>
        </QueryClientProvider>
      </AuthProvider>
    )
  );
};

describe("BCHeader", async () => {
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

  it("should render the component", async () => {
    await renderComponent();
    expect(await screen.findByTestId("header")).toBeInTheDocument();
  });

  it("should renders the site name", async () => {
    await renderComponent();
    expect(await screen.getByText("Silva")).toBeDefined();
  });

  it("opens and closes the My Profile panel", async () => {
    await renderComponent();
    const userSettingsButton = screen.getByTestId("header-button__user");
    act(() => fireEvent.click(userSettingsButton));
    expect(await screen.getByText("My Profile")).toBeDefined();
    fireEvent.click(userSettingsButton);
    // expect(screen.queryByText('My Profile')).not.toBeVisible();
  });

  it("renders the correct menu item names", async () => {
    await renderComponent();
    mainActivitiesItems.forEach((item) => {
      expect(screen.getByText(item.name)).toBeInTheDocument();
    });
  });

  it("renders sub menu items", async () => {
    await renderComponent();
    const subMenuItem = mainActivitiesItems[0].items[0]; // Assuming the first item has sub items
    const menuItem = screen.getByText(subMenuItem.name);
    act(() => fireEvent.click(menuItem));
    subMenuItem.subItems?.forEach((subSubItem) => {
      expect(screen.getByText(subSubItem.name)).toBeInTheDocument();
    });
  });
});
