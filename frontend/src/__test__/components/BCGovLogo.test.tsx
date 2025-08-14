import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import BCGovLogo from "../../components/BCGovLogo";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { PreferenceProvider } from "../../contexts/PreferenceProvider";

const queryClient = new QueryClient();
const renderWithPreferenceProvider = (component: React.JSX.Element) =>
  render(
    <QueryClientProvider client={queryClient}>
      <PreferenceProvider>{component}</PreferenceProvider>
    </QueryClientProvider>
  );

describe("BCGovLogo Component", () => {
  it("should render the image with correct src and alt text", () => {
    window.localStorage.setItem(
      "userPreference",
      JSON.stringify({ theme: "g10" })
    );

    renderWithPreferenceProvider(<BCGovLogo />);
    const logoElement = screen.getByAltText("BCGov Logo");
    expect(logoElement).toBeDefined();

    const displayedImage = document.querySelector("img") as HTMLImageElement;
    expect(displayedImage.src).toContain("bc-gov-logo.png");
  });

  it("should render the image with the correct width and class", () => {
    renderWithPreferenceProvider(<BCGovLogo />);
    const logoElement = screen.getByAltText("BCGov Logo");
    expect(logoElement.classList).toContain("logo");

    const displayedImage = document.querySelector("img") as HTMLImageElement;
    expect(displayedImage.width).toEqual(160);
  });
});
