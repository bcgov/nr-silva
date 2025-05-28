import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, it, expect, vi, beforeAll, afterAll } from "vitest";
import OpeningsMapDownloader from "@/components/OpeningsMapDownloader";

// Mock tokml
vi.mock("tokml", () => ({
  __esModule: true,
  default: vi.fn(() => "<kml>mock</kml>"),
}));

const mockCreateObjectURL = vi.fn(() => "blob:mock-url");
const mockRevokeObjectURL = vi.fn();
const originalCreateObjectURL = URL.createObjectURL;
const originalRevokeObjectURL = URL.revokeObjectURL;

describe("OpeningsMapDownloader", () => {
  beforeAll(() => {
    // @ts-ignore
    URL.createObjectURL = mockCreateObjectURL;
    // @ts-ignore
    URL.revokeObjectURL = mockRevokeObjectURL;
  });

  afterAll(() => {
    // @ts-ignore
    URL.createObjectURL = originalCreateObjectURL;
    // @ts-ignore
    URL.revokeObjectURL = originalRevokeObjectURL;
  });

  beforeEach(() => {
    mockCreateObjectURL.mockClear();
    mockRevokeObjectURL.mockClear();
  });

  const feature = {
    type: "FeatureCollection",
    features: [],
  };

  it("renders KML and GeoJSON download links", () => {
    render(<OpeningsMapDownloader feature={feature as any} />);
    expect(
      screen.getByRole("link", { name: /Download as a KML file/i })
    ).toBeInTheDocument();
    expect(
      screen.getByRole("link", { name: /Download as a GeoJSON file/i })
    ).toBeInTheDocument();
  });

  it("generates KML and GeoJSON blobs and URLs", () => {
    render(<OpeningsMapDownloader feature={feature as any} />);
    expect(mockCreateObjectURL).toHaveBeenCalledTimes(2);
    expect(screen.getByRole("link", { name: /KML/ })).toHaveAttribute(
      "href",
      "blob:mock-url"
    );
    expect(screen.getByRole("link", { name: /GeoJSON/ })).toHaveAttribute(
      "href",
      "blob:mock-url"
    );
  });

  it("revokes object URLs on unmount", () => {
    const { unmount } = render(
      <OpeningsMapDownloader feature={feature as any} />
    );
    unmount();
    expect(mockRevokeObjectURL).toHaveBeenCalledTimes(2);
    expect(mockRevokeObjectURL).toHaveBeenCalledWith("blob:mock-url");
  });
});
