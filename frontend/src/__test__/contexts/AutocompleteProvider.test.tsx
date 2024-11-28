import React from "react";
import { render, screen, waitFor, act } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, vi, expect, beforeEach, afterEach } from "vitest";
import { AutocompleteProvider, useAutocomplete } from "../../contexts/AutocompleteProvider";

// Mock lodash debounce to run instantly in tests
vi.mock("lodash", () => ({
  debounce: (fn: any) => {
    fn.cancel = vi.fn();
    return fn;
  },
}));

// Mock component to consume the context
const MockAutocompleteConsumer = () => {
  const { options, loading, error, fetchOptions } = useAutocomplete();
  return (
    <div>
      <button onClick={() => fetchOptions("test-query", "key1")}>Fetch Options</button>
      {loading && <div data-testid="loading">Loading...</div>}
      {error && <div data-testid="error">{error}</div>}
      <div data-testid="options">{JSON.stringify(options)}</div>
    </div>
  );
};

describe("AutocompleteProvider", () => {
  let fetchOptionsMock: vi.Mock;

  beforeEach(() => {
    fetchOptionsMock = vi.fn((query: string, key: string) => []);
  });

  afterEach(() => {
    vi.useRealTimers();
    vi.restoreAllMocks();
  });

  it("renders children correctly", () => {
    render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <div>Child Component</div>
      </AutocompleteProvider>
    );
    expect(screen.getByText("Child Component")).toBeInTheDocument();
  });

  it("calls fetchOptions and updates state correctly", async () => {
    fetchOptionsMock.mockResolvedValueOnce(["option1", "option2"]);

    await act(async () =>render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <MockAutocompleteConsumer />
      </AutocompleteProvider>
    ));

    await act(async () => userEvent.click(screen.getByText("Fetch Options")));

    expect(fetchOptionsMock).toHaveBeenCalledWith("test-query", "key1");

    await waitFor(() => {
      expect(screen.getByTestId("options")).toHaveTextContent(
        JSON.stringify({ key1: ["option1", "option2"] })
      );
    });
  });

  it("handles skip conditions", async () => {
    const skipConditions = { key1: (query: string) => query === "skip" };
    fetchOptionsMock.mockResolvedValueOnce(["option1", "option2"]);

    await act(async () =>render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock} skipConditions={skipConditions}>
        <MockAutocompleteConsumer />
      </AutocompleteProvider>
    ));

    await act(async () =>userEvent.click(screen.getByText("Fetch Options")));

    // Should fetch for "test-query"
    expect(fetchOptionsMock).toHaveBeenCalledWith("test-query", "key1");

    userEvent.click(screen.getByText("Fetch Options")); // Attempt to fetch for "skip"
    fetchOptionsMock.mockClear();

    await waitFor(() => {
      expect(fetchOptionsMock).not.toHaveBeenCalled(); // Skip condition met
    });
  });

  it("handles errors during fetching", async () => {
    fetchOptionsMock.mockRejectedValueOnce(new Error("Fetch error"));

    render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <MockAutocompleteConsumer />
      </AutocompleteProvider>
    );

    userEvent.click(screen.getByText("Fetch Options"));

    await waitFor(() => {
      expect(screen.getByTestId("error")).toHaveTextContent("Error fetching options");
    });
  });

  it("sets options correctly", () => {
    render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <MockAutocompleteConsumer />
      </AutocompleteProvider>
    );

    userEvent.click(screen.getByText("Fetch Options"));

    waitFor(() => {
      expect(screen.getByTestId("options")).toHaveTextContent(
        JSON.stringify({ key1: ["option1", "option2"] })
      );
    });
  });

  it("handles multiple keys correctly", async () => {
    fetchOptionsMock.mockResolvedValueOnce(["option1", "option2"]);
    fetchOptionsMock.mockResolvedValueOnce(["option3", "option4"]);

    await act(async () =>render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <MockAutocompleteConsumer />
      </AutocompleteProvider>
    ));

    await act(async () =>userEvent.click(screen.getByText("Fetch Options")));
    expect(fetchOptionsMock).toHaveBeenCalledWith("test-query", "key1");

    await waitFor(() => {
      expect(screen.getByTestId("options")).toHaveTextContent(
        JSON.stringify({ key1: ["option1", "option2"] })
      );
    });

    await act(async () =>userEvent.click(screen.getByText("Fetch Options")));
    expect(fetchOptionsMock).toHaveBeenCalledWith("test-query", "key1");

    await waitFor(() => {
      expect(screen.getByTestId("options")).toHaveTextContent(
        JSON.stringify({ key1: ["option3", "option4"] })
      );
    });
  });

  it("updates loading state correctly", async () => {
    fetchOptionsMock.mockImplementation(
      () => new Promise((resolve) => setTimeout(() => resolve(["option1", "option2"]), 2500))
    );

    await act(async () =>render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <MockAutocompleteConsumer />
      </AutocompleteProvider>
    ));
    expect(screen.queryByTestId("loading")).not.toBeInTheDocument();
    await act(async () =>userEvent.click(screen.getByText("Fetch Options")));
    expect(screen.getByTestId("loading")).toBeInTheDocument();
    await waitFor(() => expect(screen.queryByTestId("loading")).not.toBeInTheDocument(), { timeout: 3000 });
    vi.clearAllTimers();
    vi.useRealTimers();
  });

  it("updates error state correctly", async () => {
    fetchOptionsMock.mockRejectedValueOnce(new Error("Fetch error"));

    render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <MockAutocompleteConsumer />
      </AutocompleteProvider>
    );

    userEvent.click(screen.getByText("Fetch Options"));

    await waitFor(() => {
      expect(screen.getByTestId("error")).toHaveTextContent("Error fetching options");
    });
  });

  it("does not fetch options if key or query is missing", async () => {
    const MockAutocompleteConsumer1 = () => {
      const { options, loading, error, fetchOptions } = useAutocomplete();
      return (
        <div>
          <button onClick={() => fetchOptions("", "key1")}>Fetch Options</button>
          {loading && <div data-testid="loading">Loading...</div>}
          {error && <div data-testid="error">{error}</div>}
          <div data-testid="options">{JSON.stringify(options)}</div>
        </div>
      );
    };
    
    await act(async () => render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <MockAutocompleteConsumer1 />
      </AutocompleteProvider>
    ));

    await act(async () => userEvent.click(screen.getByText("Fetch Options")));

    await waitFor(() => {
      expect(fetchOptionsMock).not.toHaveBeenCalled();
    });
  });

  it("does not fetch options if condition to skip matches", async () => {
    const MockAutocompleteConsumer2 = () => {
      const { options, loading, error, fetchOptions } = useAutocomplete();
      return (
        <div>
          <button onClick={() => fetchOptions("query", "key1")}>Fetch Options</button>
          {loading && <div data-testid="loading">Loading...</div>}
          {error && <div data-testid="error">{error}</div>}
          <div data-testid="options">{JSON.stringify(options)}</div>
        </div>
      );
    };

    const conditions = {
      key1: (query: string) => query === "query"
    }
    
    await act(async () => render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock} skipConditions={conditions}>
        <MockAutocompleteConsumer2 />
      </AutocompleteProvider>
    ));

    await act(async () => userEvent.click(screen.getByText("Fetch Options")));

    await waitFor(() => {
      expect(fetchOptionsMock).not.toHaveBeenCalled();
    });
  });

  it("should fail when mounting without context", () => {
    expect(() => render(<MockAutocompleteConsumer />)).toThrow(
      "useAutocomplete must be used within an AutocompleteProvider"
    );
  })

  it("handles no results with proper message", async () => {
    fetchOptionsMock.mockResolvedValueOnce([]);

    await act(async () =>render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <MockAutocompleteConsumer />
      </AutocompleteProvider>
    ));

    await act(async () => userEvent.click(screen.getByText("Fetch Options")));

    expect(fetchOptionsMock).toHaveBeenCalledWith("test-query", "key1");

    await waitFor(() => {
      expect(screen.getByTestId("options")).toHaveTextContent(
        JSON.stringify({ key1: [{"id":"","label":"No results found"}] })
      );
    });
  });

  it("handles set options", async () => {
    fetchOptionsMock.mockResolvedValueOnce(["option1", "option2"]);

    const MockAutocompleteConsumer3 = () => {
      const { options, loading, error, fetchOptions, updateOptions } = useAutocomplete();
      return (
        <div>
          <button onClick={() => fetchOptions("query", "key1")}>Fetch Options</button>
          <button onClick={() => updateOptions("key1", ["notalot"])}>Set Options</button>
          {loading && <div data-testid="loading">Loading...</div>}
          {error && <div data-testid="error">{error}</div>}
          <div data-testid="options">{JSON.stringify(options)}</div>
        </div>
      );
    };
    
    await act(async () => render(
      <AutocompleteProvider fetchOptions={fetchOptionsMock}>
        <MockAutocompleteConsumer3 />
      </AutocompleteProvider>
    ));

    await act(async () => userEvent.click(screen.getByText("Fetch Options")));

    expect(fetchOptionsMock).toHaveBeenCalledWith("query", "key1");

    await waitFor(() => {
      expect(screen.getByTestId("options")).toHaveTextContent(
        JSON.stringify({ key1: ["option1", "option2"] })
      );
    });

    await act(async () => userEvent.click(screen.getByText("Set Options")));


    await waitFor(() => {
      expect(screen.getByTestId("options")).toHaveTextContent(
        JSON.stringify({ key1: ["notalot"] })
      );
    });

  });
});