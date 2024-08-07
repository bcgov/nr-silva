package ca.bc.gov.restapi.results.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** This class contains base configuration for Swagger API documentation. */
@Configuration
public class SwaggerConfig {

  private static final String DESCRIPTION =
      "A REST service API to provide SILVA Application database and backend capabilities.";
  private static final String TERMS_OF_SERVICE =
      "https://www2.gov.bc.ca/gov/content/data/open-data/api-terms-of-use-for-ogl-information";
  private static final String LICENSE_URL =
      "https://www2.gov.bc.ca/gov/content/data/open-data/open-government-licence-bc";

  /**
   * Creates an {@link OpenAPI} with all needed and related information.
   *
   * @return An {@link OpenAPI} instance
   */
  @Bean
  public OpenAPI theRestApi() {
    Info info = new Info();
    info.setTitle("SILVA Back-end REST API");
    info.setDescription(DESCRIPTION);
    info.setVersion("0.0.1");
    info.setTermsOfService(TERMS_OF_SERVICE);

    Contact contact = new Contact();
    contact.setName("Team Silva");
    contact.setEmail("team.silva@gov.bc.ca");
    contact.setUrl("https://github.com/bcgov/nr-silva");
    info.setContact(contact);

    License license = new License();
    license.setName("OGL-BC");
    license.setUrl(LICENSE_URL);
    info.setLicense(license);

    ExternalDocumentation externalDoc = new ExternalDocumentation();
    externalDoc.setDescription("Swagger login How-To");
    externalDoc.setUrl("https://github.com/bcgov/nr-silva/wiki/Getting-a-Bearer-JWT-Token");

    SecurityScheme securityScheme = new SecurityScheme();
    securityScheme.setType(Type.HTTP);
    securityScheme.setScheme("bearer");
    securityScheme.setBearerFormat("JWT");

    Components components = new Components();
    components.addSecuritySchemes("bearerAuth", securityScheme);

    OpenAPI openApi = new OpenAPI();
    openApi.setInfo(info);
    openApi.setExternalDocs(externalDoc);
    openApi.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    openApi.setComponents(components);

    return openApi;
  }
}
