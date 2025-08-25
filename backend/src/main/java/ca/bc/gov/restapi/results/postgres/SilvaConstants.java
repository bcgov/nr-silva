package ca.bc.gov.restapi.results.postgres;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** This class holds properties used for the application configuration. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaConstants {

  public static final Integer MAX_OPENING_FILE_SIZE_BYTES = 25 * 1024 * 1024; // 25 MB
}
