import React from "react";
import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import axios from "axios";
import OpeningAttachment from "@/components/OpeningDetails/OpeningAttachment";
import API from "@/services/API";
import { OpeningDetailsAttachmentMetaDto } from "@/services/OpenApi";
import * as CookieUtils from "@/utils/CookieUtils";

vi.mock("axios");
vi.mock("@/services/API", () => ({
  default: {
    OpeningEndpointService: {
      getAttachments: vi.fn(),
    },
  },
}));

describe("OpeningAttachment", () => {
  let queryClient: QueryClient;

  const mockAttachments: OpeningDetailsAttachmentMetaDto[] = [
    {
      attachmentGuid: "guid-123",
      attachmentName: "test-map.pdf",
      attachmentDescription: "Opening site map",
      mimeTypeCode: "application/pdf",
      attachmentSize: 1024,
      entryTimestamp: "2023-01-01T10:00:00",
      entryUserId: "USER1",
      updateTimestamp: "2023-01-05T12:30:00",
      updateUserId: "USER2",
      revisionCount: 1,
    },
    {
      attachmentGuid: "guid-456",
      attachmentName: "report.docx",
      attachmentDescription: "",
      mimeTypeCode: "application/docx",
      attachmentSize: 2048,
      entryTimestamp: "2023-02-01T10:00:00",
      entryUserId: "USER1",
      updateTimestamp: "2023-02-05T12:30:00",
      updateUserId: "USER3",
      revisionCount: 1,
    },
  ];

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false,
        },
      },
    });
    vi.clearAllMocks();
    vi.spyOn(CookieUtils, "getCookie").mockReturnValue("test-token");
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  const renderComponent = (openingId: number = 101) => {
    return render(
      <QueryClientProvider client={queryClient}>
        <OpeningAttachment openingId={openingId} />
      </QueryClientProvider>
    );
  };

  it("renders loading skeleton when query is in progress", () => {
    vi.mocked(API.OpeningEndpointService.getAttachments).mockImplementation(
      () => new Promise(() => {}) // never resolves
    );

    renderComponent(101);

    expect(screen.getByText(/\.\.\.\s+attachments\s+in this opening/i)).toBeInTheDocument();
    expect(screen.getByRole("table", { name: /loading table data/i })).toBeInTheDocument();
  });

  it("renders empty state when there are no attachments", async () => {
    vi.mocked(API.OpeningEndpointService.getAttachments).mockResolvedValue([]);

    renderComponent(101);

    await waitFor(() => {
      expect(screen.getByText("Nothing to show yet!")).toBeInTheDocument();
    });

    expect(
      screen.getByText("No attachments have been added to this opening yet.")
    ).toBeInTheDocument();
  });

  it("renders attachment table with formatted content and placeholders", async () => {
    vi.mocked(API.OpeningEndpointService.getAttachments).mockResolvedValue(mockAttachments);

    renderComponent(101);

    await waitFor(() => {
      expect(screen.getByText(/2\s+attachments\s+in this opening/i)).toBeInTheDocument();
    });

    // Verify table headers
    expect(screen.getByText("File name")).toBeInTheDocument();
    expect(screen.getByText("Description")).toBeInTheDocument();
    expect(screen.getByText("Updated at")).toBeInTheDocument();
    expect(screen.getByText("Updated by")).toBeInTheDocument();
    expect(screen.getByText("Action")).toBeInTheDocument();

    // Verify row contents
    expect(screen.getByText("test-map.pdf")).toBeInTheDocument();
    expect(screen.getByText("Opening site map")).toBeInTheDocument();
    expect(screen.getByText("USER2")).toBeInTheDocument();

    expect(screen.getByText("report.docx")).toBeInTheDocument();
    expect(screen.getByText("USER3")).toBeInTheDocument();
    // Empty description should render PLACE_HOLDER "--"
    expect(screen.getByText("--")).toBeInTheDocument();

    // Verify download buttons
    const downloadButtons = screen.getAllByRole("button", { name: /Download/i });
    expect(downloadButtons).toHaveLength(2);
  });

  it("triggers file download successfully on Download button click", async () => {
    vi.mocked(API.OpeningEndpointService.getAttachments).mockResolvedValue(mockAttachments);
    const mockS3Url = "https://s3.aws.amazon.com/bucket/test-map.pdf?signature=123";
    vi.mocked(axios.get).mockResolvedValue({ data: mockS3Url });

    const originalClick = HTMLAnchorElement.prototype.click;
    const clickMock = vi.fn();
    HTMLAnchorElement.prototype.click = clickMock;

    try {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("test-map.pdf")).toBeInTheDocument();
      });

      const downloadBtn = screen.getByRole("button", { name: "Download test-map.pdf" });
      fireEvent.click(downloadBtn);

      await waitFor(() => {
        expect(axios.get).toHaveBeenCalledWith(
          expect.stringContaining("/api/openings/101/attachments/guid-123"),
          {
            headers: {
              Authorization: "Bearer test-token",
            },
          }
        );
        expect(clickMock).toHaveBeenCalled();
      });
    } finally {
      HTMLAnchorElement.prototype.click = originalClick;
    }
  });

  it("handles download error gracefully without crashing", async () => {
    vi.mocked(API.OpeningEndpointService.getAttachments).mockResolvedValue(mockAttachments);
    const consoleErrorSpy = vi.spyOn(console, "error").mockImplementation(() => {});
    vi.mocked(axios.get).mockRejectedValue(new Error("Network Error"));

    renderComponent(101);

    await waitFor(() => {
      expect(screen.getByText("test-map.pdf")).toBeInTheDocument();
    });

    const downloadBtn = screen.getByRole("button", { name: "Download test-map.pdf" });
    fireEvent.click(downloadBtn);

    await waitFor(() => {
      expect(consoleErrorSpy).toHaveBeenCalledWith(
        "File download failed:",
        expect.any(Error)
      );
    });

    consoleErrorSpy.mockRestore();
  });
});
