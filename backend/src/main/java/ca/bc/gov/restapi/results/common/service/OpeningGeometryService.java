package ca.bc.gov.restapi.results.common.service;

import org.geojson.FeatureCollection;

/** Provides opening geometry as a GeoJSON FeatureCollection. */
public interface OpeningGeometryService {

  FeatureCollection getOpeningGeometry(Long openingId);
}
