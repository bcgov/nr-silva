import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import {
  PreferenceProvider,
  usePreference,
} from "@/contexts/PreferenceProvider";
import { UserPreference } from "@/types/PreferencesType";

// Mock localStorageUtils
vi.mock("@/utils/localStorageUtils", () => {
  return {
    loadUserPreference: vi.fn(() => ({
      theme: "g10",
      visibleColumns: {
        silvicultureSearch: [{ key: "foo", header: "Foo", selected: true }],
      },
    })),
    saveUserPreference: vi.fn((pref) => pref),
  };
});

function TestComponent() {
  const { userPreference, updatePreferences } = usePreference();
  return (
    <div>
      <span data-testid="theme">{userPreference.theme}</span>
      <button
        onClick={() => updatePreferences({ theme: "g100" })}
        data-testid="update-btn"
      >
        Update
      </button>
    </div>
  );
}

const queryClient = new QueryClient();

describe("PreferenceProvider", () => {
  it("provides userPreference from localStorage", () => {
    render(
      <QueryClientProvider client={queryClient}>
        <PreferenceProvider>
          <TestComponent />
        </PreferenceProvider>
      </QueryClientProvider>
    );
    expect(screen.getByTestId("theme").textContent).toBe("g10");
  });

  it("updates userPreference when updatePreferences is called", () => {
    render(
      <QueryClientProvider client={queryClient}>
        <PreferenceProvider>
          <TestComponent />
        </PreferenceProvider>
      </QueryClientProvider>
    );
    const btn = screen.getByTestId("update-btn");
    fireEvent.click(btn);
    expect(screen.getByTestId("theme").textContent).toBe("g100");
  });

  it("throws error if usePreference is used outside provider", () => {
    // Suppress error output for this test
    const spy = vi.spyOn(console, "error").mockImplementation(() => {});
    function BadComponent() {
      usePreference();
      return null;
    }
    expect(() => render(<BadComponent />)).toThrow(
      /usePreference must be used within a PreferenceProvider/
    );
    spy.mockRestore();
  });
});
