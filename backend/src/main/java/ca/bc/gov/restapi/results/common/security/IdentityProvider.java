package ca.bc.gov.restapi.results.common.security;

import java.util.Arrays;
import java.util.Optional;

/** Enumeration of the identity providers our application works with. */
public enum IdentityProvider {
  IDIR("idir"),
  BUSINESS_BCEID("bceidbusiness");

  private final String claimName;

  IdentityProvider(String claimName) {
    this.claimName = claimName;
  }

  /**
   * Extract the identity provider from a Jwt.
   *
   * @param provider The provider name
   * @return the identity provider, if one is found
   */
  public static Optional<IdentityProvider> fromClaim(String provider) {
    return Arrays.stream(values())
        .filter(enumValue -> enumValue.claimName.equals(provider))
        .findFirst();
  }
}
