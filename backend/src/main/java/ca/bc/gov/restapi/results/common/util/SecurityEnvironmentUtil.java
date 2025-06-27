package ca.bc.gov.restapi.results.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class contains utility methods for checking the environment of the application.
 * It is used to determine if the application is running in a local environment.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityEnvironmentUtil {
  private static final String LOCAL_ENVIRONMENT = "local";

  public static boolean isLocalEnvironment(String environment) {
    return LOCAL_ENVIRONMENT.equalsIgnoreCase(environment);
  }
}
