import { render, screen, waitFor } from "@testing-library/react";
import axios from "axios";
import { usePostViewedOpening, postViewedOpening } from "../../../../services/queries/dashboard/dashboardQueries";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { vi, describe, it, expect } from "vitest";
import { getAuthIdToken } from "../../../../services/AuthService";

// Mock axios and the AuthService function
vi.mock("axios");
vi.mock("../../../../services/AuthService", () => ({
  getAuthIdToken: vi.fn(),
}));

describe("postViewedOpening", () => {
  const backendUrl = import.meta.env.VITE_BACKEND_URL;
  const openingId = "123";

  it("should send a PUT request and return data on success", async () => {
    const mockResponse = { data: { message: "Success" } };
    (axios.put as vi.Mock).mockResolvedValue(mockResponse);
    (getAuthIdToken as vi.Mock).mockReturnValue("testAuthToken");

    const result = await postViewedOpening(openingId);

    expect(axios.put).toHaveBeenCalledWith(`${backendUrl}/api/openings/recent/${openingId}`, null, {
      headers: { Authorization: `Bearer testAuthToken` },
    });
    expect(result).toEqual(mockResponse.data);
  });

  it("should throw a 403 error if the user is unauthorized", async () => {
    (axios.put as vi.Mock).mockRejectedValue({ response: { status: 403 } });

    await expect(postViewedOpening(openingId)).rejects.toThrow(
      "Forbidden: You don't have permission to view this opening."
    );
  });

  it("should throw a specific error message for other server errors", async () => {
    const errorMessage = "Server error occurred";
    (axios.put as vi.Mock).mockRejectedValue({
      response: { data: { message: errorMessage } },
    });

    await expect(postViewedOpening(openingId)).rejects.toThrow(errorMessage);
  });
});

describe("usePostViewedOpening", () => {
  const queryClient = new QueryClient();

  const wrapper = ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );

  // Mock Component to test the hook
  const MockComponent = ({ openingId }: { openingId: string }) => {
    const mutation = usePostViewedOpening();

    return (
      <button
        onClick={() => mutation.mutate(openingId)}
        data-testid="mutate-button"
      >
        Mutate
      </button>
    );
  };

  it("should call postViewedOpening when mutate is invoked", async () => {
    const mockResponse = { message: "Success" };
    const backendUrl = import.meta.env.VITE_BACKEND_URL;
    (axios.put as vi.Mock).mockResolvedValue({ data: mockResponse });
    (getAuthIdToken as vi.Mock).mockReturnValue("testAuthToken");

    render(
      <QueryClientProvider client={queryClient}>
        <MockComponent openingId="123" />
      </QueryClientProvider>
    );

    // Trigger the mutation
    screen.getByTestId("mutate-button").click();

    // Wait for axios call
    await waitFor(() =>
      expect(axios.put).toHaveBeenCalledWith(`${backendUrl}/api/openings/recent/123`, null, {
        headers: { Authorization: `Bearer testAuthToken` },
      })
    );
  });

  it("should handle errors when mutation fails", async () => {
    const errorMessage = "Server error occurred";
    (axios.put as vi.Mock).mockRejectedValue({
      response: { data: { message: errorMessage } },
    });

    render(
      <QueryClientProvider client={queryClient}>
        <MockComponent openingId="123" />
      </QueryClientProvider>
    );

    // Trigger the mutation
    screen.getByTestId("mutate-button").click();

    await waitFor(() => {
      expect(axios.put).toHaveBeenCalled();
    });
  });
});
