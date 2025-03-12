package ca.bc.gov.restapi.results.oracle.entity;

public interface OpeningTrendsProjection {

  int getYear();

  int getMonth();

  String getStatus();

  long getCount();
}
