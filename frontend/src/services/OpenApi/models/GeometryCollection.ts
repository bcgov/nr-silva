/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Feature } from './Feature';
import type { FeatureCollection } from './FeatureCollection';
import type { GeoJsonObject } from './GeoJsonObject';
import type { LineString } from './LineString';
import type { MultiLineString } from './MultiLineString';
import type { MultiPoint } from './MultiPoint';
import type { MultiPolygon } from './MultiPolygon';
import type { Point } from './Point';
import type { Polygon } from './Polygon';
export type GeometryCollection = (GeoJsonObject & {
    geometries?: Array<(Feature | FeatureCollection | GeometryCollection | LineString | MultiLineString | MultiPoint | MultiPolygon | Point | Polygon)>;
});

