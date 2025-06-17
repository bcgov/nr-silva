/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Crs } from './Crs';
import type { FeatureCollection } from './FeatureCollection';
import type { GeometryCollection } from './GeometryCollection';
import type { LineString } from './LineString';
import type { MultiLineString } from './MultiLineString';
import type { MultiPoint } from './MultiPoint';
import type { MultiPolygon } from './MultiPolygon';
import type { Point } from './Point';
import type { Polygon } from './Polygon';
export type Feature = {
    crs?: Crs;
    bbox?: Array<number>;
    properties?: Record<string, any>;
    geometry?: (Feature | FeatureCollection | GeometryCollection | LineString | MultiLineString | MultiPoint | MultiPolygon | Point | Polygon);
    id?: string;
};

