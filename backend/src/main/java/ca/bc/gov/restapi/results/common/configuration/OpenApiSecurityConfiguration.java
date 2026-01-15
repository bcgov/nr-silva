package ca.bc.gov.restapi.results.common.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Adds a bearer JWT security scheme to the OpenAPI definition so Swagger UI presents an Authorize
 * dialog and will send the provided token in Authorization header for requests.
 */
@Configuration
public class OpenApiSecurityConfiguration {

  private static final String SECURITY_SCHEME_NAME = "bearerAuth";

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
        .info(new Info().title("NRIDS Silva API").version("v1"))
        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
        .schemaRequirement(
            SECURITY_SCHEME_NAME,
            new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));
  }
}
