package ca.bc.gov.restapi.results.extensions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

/** Marks a test class or method with a mock Keycloak-style JWT security context. */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockJwtSecurityContextFactory.class)
public @interface WithMockJwt {
  /** Username / subject identifier for the mock JWT. */
  String value() default "test";

  /** Legacy Cognito group names (merged with {@link #roles()}). */
  String[] cognitoGroups() default {};

  /** CSS / Keycloak role names for the mock JWT. */
  String[] roles() default {};

  /** Email claim value. */
  String email() default "test@test.ca";

  /** Identity provider claim (e.g. {@code idir}, {@code azureidir}, {@code bceidbusiness}). */
  String idp() default "idir";

  /** Display name claim value. */
  String displayName() default "Test, Automated WLRS:EX";

  /** Authorized party ({@code azp}) claim value. */
  String azp() default "nr-silva";
}
