package ca.bc.gov.restapi.results.common.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** This class holds properties used for the application configuration. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantsConfig {

  public static final Integer MAX_PAGE_SIZE = 500;
  public static final Integer MAX_PAGE_SIZE_OPENING_SEARCH = 2000;
}
