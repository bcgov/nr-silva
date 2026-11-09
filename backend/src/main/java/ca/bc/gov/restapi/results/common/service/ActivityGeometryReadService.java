package ca.bc.gov.restapi.results.common.service;

import org.geojson.FeatureCollection;

/** Provides the geometries of the (non-disturbance) activities of an opening as GeoJSON. */
public interface ActivityGeometryReadService {

  FeatureCollection getActivityGeometries(Long openingId);
}
