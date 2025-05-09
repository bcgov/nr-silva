package ca.bc.gov.restapi.results.postgres.service;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.bc.gov.restapi.results.common.exception.InvalidOpeningIdException;
import ca.bc.gov.restapi.results.common.exception.OpeningNotFoundException;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.repository.UserRecentOpeningRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DisplayName("Unit Test | UserRecentOpeningService")
@WithMockJwt("JAKETHEDOG")
@TestMethodOrder(OrderAnnotation.class)
class UserRecentOpeningSearchServiceTest extends AbstractTestContainerIntegrationTest {

  public static final Long OPENING_ID = 101017L;
  public static final String IDIR_TEST = "IDIR\\JAKETHEDOG";

  @Autowired
  private UserRecentOpeningRepository userRecentOpeningRepository;

  @Autowired
  private UserRecentOpeningService userRecentOpeningService;

  @RegisterExtension
  static WireMockExtension clientApiStub = WireMockExtension
      .newInstance()
      .options(
          wireMockConfig()
              .port(10000)
              .notifier(new WiremockLogNotifier())
              .asynchronousResponseEnabled(true)
              .stubRequestLoggingDisabled(false)
      )
      .configureStaticDsl(true)
      .build();

  @Test
  @Order(1)
  @DisplayName("1 | storeViewedOpening | list openings | empty list")
  void getAllRecentOpeningsForUser_noRecentOpenings_returnsEmptyResult() {
    Page<OpeningSearchResponseDto> result = userRecentOpeningService.getAllRecentOpeningsForUser(
        PageRequest.of(0, 10));
    assertNotNull(result);
    assertThat(result.getContent()).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("2 | storeViewedOpening | new opening | saves entity")
  @Order(2)
  void storeViewedOpening_newOpening_savesEntity() {
    UserRecentOpeningDto result = userRecentOpeningService.storeViewedOpening(OPENING_ID);
    assertNotNull(result);
    assertEquals(IDIR_TEST, result.userId());
    assertEquals(OPENING_ID, result.openingId());
    assertNotNull(result.lastViewed());
  }

  @Test
  @DisplayName("3 | storeViewedOpening | existing opening | updates entity")
  @Order(3)
  void storeViewedOpening_existingOpening_updatesEntity() {
    storeViewedOpening_newOpening_savesEntity();
  }

  @Test
  @DisplayName("4 | storeViewedOpening | invalid opening ID | throws exception")
  @Order(4)
  void storeViewedOpening_invalidOpeningId_throwsException() {
    Long invalidOpeningId = null;

    Exception exception = assertThrows(InvalidOpeningIdException.class, () -> {
      userRecentOpeningService.storeViewedOpening(invalidOpeningId);
    });

    assertEquals("417 EXPECTATION_FAILED \"Opening ID must contain numbers only!\"", exception.getMessage());
  }

  @Test
  @DisplayName("5 | storeViewedOpening | opening not found | throws exception")
  @Order(5)
  void storeViewedOpening_openingIdNotFound_throwsException() {
    assertThrows(OpeningNotFoundException.class, () -> {
      userRecentOpeningService.storeViewedOpening(7745L);
    });
  }

  @Test
  @Order(6)
  @DisplayName("6 | storeViewedOpening | list openings | results found")
  void getAllRecentOpeningsForUser_noRecentOpenings_returnsResult() {
    String clientNumber = "00000003";
    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/findByClientNumber/" + clientNumber))
            .willReturn(okJson("""
                {
                  "clientNumber": "00000003",
                  "clientName": "MINISTRY OF FORESTS",
                  "legalFirstName": null,
                  "legalMiddleName": null,
                  "clientStatusCode": "ACT",
                  "clientTypeCode": "F",
                  "acronym": "MOF"
                }
                """)
            )
    );

    Page<OpeningSearchResponseDto> result = userRecentOpeningService.getAllRecentOpeningsForUser(PageRequest.of(0,10));
    assertNotNull(result);
    assertThat(result.getContent()).isNotNull().isNotEmpty();
  }

}
