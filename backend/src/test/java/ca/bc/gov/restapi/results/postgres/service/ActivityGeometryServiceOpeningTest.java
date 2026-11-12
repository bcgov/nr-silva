package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.postgres.entity.OpeningGeometryEntity;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningGeometryPostgresRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | ActivityGeometryService | Within opening")
class ActivityGeometryServiceOpeningTest {

  private static final Long OPENING_ID = 1001L;
  private static final String OPENING_SQUARE = "POLYGON((0 0, 100 0, 100 100, 0 100, 0 0))";

  @Mock private SpatialFileService spatialFileService;
  @Mock private ActivityTreatmentGeometryPostgresRepository geometryRepository;
  @Mock private OpeningGeometryPostgresRepository openingGeometryRepository;

  private ActivityGeometryService service;

  @BeforeEach
  void setUp() {
    service =
        new ActivityGeometryService(
            spatialFileService, geometryRepository, openingGeometryRepository, new ObjectMapper());
  }

  private static Geometry wkt(String wkt) throws ParseException {
    return new WKTReader().read(wkt);
  }

  private void givenOpening(String wkt) throws ParseException {
    when(openingGeometryRepository.findById(OPENING_ID))
        .thenReturn(
            Optional.of(OpeningGeometryEntity.builder().geometry(wkt(wkt)).build()));
  }

  private void assertRejected(String activityWkt) {
    assertThatThrownBy(() -> service.validateWithinOpening(OPENING_ID, wkt(activityWkt)))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            e ->
                org.assertj.core.api.Assertions.assertThat(e.getReason())
                    .startsWith("Activity geometry must be within the opening geometry"));
  }

  private void assertRejected(String activityWkt, String expectedSuffix) {
    assertThatThrownBy(() -> service.validateWithinOpening(OPENING_ID, wkt(activityWkt)))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            e -> {
              org.assertj.core.api.Assertions.assertThat(e.getStatusCode())
                  .isEqualTo(HttpStatus.BAD_REQUEST);
              org.assertj.core.api.Assertions.assertThat(e.getReason())
                  .isEqualTo("Activity geometry must be within the opening geometry" + expectedSuffix);
            });
  }

  private void assertAccepted(String activityWkt) {
    assertThatCode(() -> service.validateWithinOpening(OPENING_ID, wkt(activityWkt)))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Passes when the activity is inside the opening")
  void inside_passes() throws ParseException {
    givenOpening(OPENING_SQUARE);
    assertAccepted("POLYGON((10 10, 50 10, 50 50, 10 50, 10 10))");
  }

  @Test
  @DisplayName("Passes when the activity touches or equals the opening boundary")
  void touchingBoundary_passes() throws ParseException {
    givenOpening(OPENING_SQUARE);
    assertAccepted("POLYGON((0 0, 50 0, 50 50, 0 50, 0 0))");
    assertAccepted(OPENING_SQUARE);
  }

  @Test
  @DisplayName("Passes when the activity is outside by less than the 4 m tolerance")
  void withinTolerance_passes() throws ParseException {
    givenOpening(OPENING_SQUARE);
    assertAccepted("POLYGON((0 0, 103.9 0, 103.9 50, 0 50, 0 0))");
  }

  @Test
  @DisplayName("Rejects beyond the tolerance and reports how far outside the activity reaches")
  void beyondTolerance_rejectedWithOvershoot() throws ParseException {
    givenOpening(OPENING_SQUARE);
    assertRejected("POLYGON((0 0, 104.5 0, 104.5 50, 0 50, 0 0))", " (up to 4.5 m outside)");
  }

  @Test
  @DisplayName("Rejects when the activity is partly outside the opening")
  void partlyOutside_rejected() throws ParseException {
    givenOpening(OPENING_SQUARE);
    assertRejected("POLYGON((50 50, 150 50, 150 90, 50 90, 50 50))", " (up to 50.0 m outside)");
  }

  @Test
  @DisplayName("Rejects when the activity is completely outside the opening")
  void outside_rejected() throws ParseException {
    givenOpening(OPENING_SQUARE);
    assertRejected(
        "POLYGON((200 200, 250 200, 250 250, 200 250, 200 200))", " (up to 212.1 m outside)");
  }

  @Test
  @DisplayName("MultiPolygon activity passes when every part is inside, fails otherwise")
  void multiPolygonActivity() throws ParseException {
    givenOpening(OPENING_SQUARE);
    assertAccepted(
        "MULTIPOLYGON(((10 10, 20 10, 20 20, 10 20, 10 10)),"
            + "((60 60, 70 60, 70 70, 60 70, 60 60)))");
    assertRejected(
        "MULTIPOLYGON(((10 10, 20 10, 20 20, 10 20, 10 10)),"
            + "((300 300, 310 300, 310 310, 300 310, 300 300)))");
  }

  @Test
  @DisplayName("MultiPolygon opening is treated as a union of its parts")
  void multiPolygonOpening_union() throws ParseException {
    givenOpening(
        "MULTIPOLYGON(((0 0, 50 0, 50 100, 0 100, 0 0)),((50 0, 100 0, 100 100, 50 100, 50 0)))");
    assertAccepted("POLYGON((40 10, 60 10, 60 20, 40 20, 40 10))");
    assertRejected("POLYGON((40 10, 160 10, 160 20, 40 20, 40 10))");
  }

  @Test
  @DisplayName("Passes when the opening has no geometry row")
  void noOpeningRow_passes() {
    when(openingGeometryRepository.findById(OPENING_ID)).thenReturn(Optional.empty());
    assertAccepted("POLYGON((200 200, 250 200, 250 250, 200 250, 200 200))");
  }

  @Test
  @DisplayName("Passes when the opening geometry is null or empty")
  void nullOrEmptyOpeningGeometry_passes() throws ParseException {
    when(openingGeometryRepository.findById(OPENING_ID))
        .thenReturn(Optional.of(OpeningGeometryEntity.builder().build()));
    assertAccepted("POLYGON((200 200, 250 200, 250 250, 200 250, 200 200))");

    givenOpening("POLYGON EMPTY");
    assertAccepted("POLYGON((200 200, 250 200, 250 250, 200 250, 200 200))");
  }

  @Test
  @DisplayName("Repairs an invalid opening geometry instead of failing")
  void invalidOpeningGeometry_isRepaired() throws ParseException {
    // Bow-tie polygon: self-intersecting, repaired by buffer(0) to the larger lobe
    givenOpening("POLYGON((0 0, 100 100, 100 0, 0 100, 0 0))");
    assertAccepted("POLYGON((90 40, 99 40, 99 60, 90 60, 90 40))");
  }
}
