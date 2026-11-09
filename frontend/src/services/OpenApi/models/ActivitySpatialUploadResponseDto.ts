/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JsonNode } from './JsonNode';
export type ActivitySpatialUploadResponseDto = {
    /**
     * Activity geometry area in hectares
     */
    geometryArea?: number;
    /**
     * Activity geometry as GeoJSON in EPSG:4326
     */
    geoJson?: JsonNode;
    /**
     * Opening geometry as a GeoJSON feature collection in EPSG:4326; null when the opening has no geometry
     */
    openingGeoJson?: JsonNode;
};

