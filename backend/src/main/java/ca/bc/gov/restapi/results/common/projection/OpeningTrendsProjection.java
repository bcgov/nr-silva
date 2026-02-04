package ca.bc.gov.restapi.results.common.projection;

public interface OpeningTrendsProjection {

  int getYear();

  int getMonth();

  String getStatus();

  long getCount();
}
