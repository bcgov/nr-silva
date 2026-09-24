import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, it, expect, vi, beforeAll, afterAll } from "vitest";
import OpeningsMapDownloader from "../../components/OpeningsMapDownloader";

const mockReadFeatures = vi.fn(() => []);
const mockWriteFeatures = vi.fn(() => "<kml>mock</kml>");

// Mock ol/format/GeoJSON and ol/format/KML
vi.mock("ol/format/GeoJSON", () => ({
  __esModule: true,
  default: vi.fn(function () {
    return {
      readFeatures: mockReadFeatures,
    };
  }),
}));

vi.mock("ol/format/KML", () => ({
  __esModule: true,
  default: vi.fn(function () {
    return {
      writeFeatures: mockWriteFeatures,
    };
  }),
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

  it("handles projection from feature with EPSG code", () => {
    const featureWithEpsg = {
      type: "FeatureCollection",
      crs: {
        properties: {
          name: "urn:ogc:def:crs:EPSG:3005",
        },
      },
      features: [],
    };

    render(<OpeningsMapDownloader feature={featureWithEpsg as any} />);
    expect(screen.getByRole("link", { name: /KML/ })).toBeInTheDocument();
    expect(mockReadFeatures).toHaveBeenCalledWith(featureWithEpsg, {
      dataProjection: "EPSG:3005",
      featureProjection: "EPSG:3005",
    });
    expect(mockWriteFeatures).toHaveBeenCalledWith([], {
      dataProjection: "EPSG:3005",
      featureProjection: "EPSG:3005",
    });
  });

  it("falls back to EPSG:4326 when crs has no EPSG", () => {
    const featureNonEpsg = {
      type: "FeatureCollection",
      crs: {
        properties: {
          name: "urn:ogc:def:crs:OGC:1.3:CRS84",
        },
      },
      features: [],
    };

    render(<OpeningsMapDownloader feature={featureNonEpsg as any} />);
    expect(screen.getByRole("link", { name: /KML/ })).toBeInTheDocument();
    expect(mockReadFeatures).toHaveBeenCalledWith(featureNonEpsg, {
      dataProjection: "EPSG:4326",
      featureProjection: "EPSG:4326",
    });
    expect(mockWriteFeatures).toHaveBeenCalledWith([], {
      dataProjection: "EPSG:4326",
      featureProjection: "EPSG:4326",
    });
  });

  it("falls back to EPSG:4326 when crs has no digits in EPSG", () => {
    const featureInvalidEpsg = {
      type: "FeatureCollection",
      crs: {
        properties: {
          name: "EPSG:XYZ",
        },
      },
      features: [],
    };

    render(<OpeningsMapDownloader feature={featureInvalidEpsg as any} />);
    expect(screen.getByRole("link", { name: /KML/ })).toBeInTheDocument();
    expect(mockReadFeatures).toHaveBeenCalledWith(featureInvalidEpsg, {
      dataProjection: "EPSG:4326",
      featureProjection: "EPSG:4326",
    });
    expect(mockWriteFeatures).toHaveBeenCalledWith([], {
      dataProjection: "EPSG:4326",
      featureProjection: "EPSG:4326",
    });
  });
});
