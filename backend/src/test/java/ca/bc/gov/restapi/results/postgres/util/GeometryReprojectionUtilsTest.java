package ca.bc.gov.restapi.results.postgres.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

class GeometryReprojectionUtilsTest {

  private static final double GEOTOOLS_BASELINE_TOLERANCE_METRES = 0.5;
  private static final double GEOTOOLS_BASELINE_TOLERANCE_DEGREES = 0.000_005;
  private static final double ROUND_TRIP_TOLERANCE_DEGREES = 0.000_000_1;

  private final GeometryFactory geometryFactory = new GeometryFactory();

  @Test
  void to3005MatchesGeoToolsBaselineAndDoesNotMutateInput() {
    Point source = geometryFactory.createPoint(new Coordinate(-123.3656, 48.4284, 42));
    source.setSRID(GeometryReprojectionUtils.SRID_4326);
    Geometry original = source.copy();

    Point transformed = (Point) GeometryReprojectionUtils.to3005(source);

    assertThat(transformed.getX())
        .isCloseTo(1_195_327.902_761_101_7, within(GEOTOOLS_BASELINE_TOLERANCE_METRES));
    assertThat(transformed.getY())
        .isCloseTo(382_812.073_968_546_5, within(GEOTOOLS_BASELINE_TOLERANCE_METRES));
    assertThat(transformed.getCoordinate().getZ()).isEqualTo(42);
    assertThat(transformed.getSRID()).isEqualTo(GeometryReprojectionUtils.SRID_3005);
    assertThat(source.equalsExact(original)).isTrue();
    assertThat(source.getSRID()).isEqualTo(GeometryReprojectionUtils.SRID_4326);
  }

  @Test
  void to4326MatchesGeoToolsBaseline() {
    Point source = geometryFactory.createPoint(new Coordinate(500_000, 1_700_000));
    source.setSRID(GeometryReprojectionUtils.SRID_3005);

    Point transformed = (Point) GeometryReprojectionUtils.to4326(source);

    assertThat(transformed.getX())
        .isCloseTo(-134.960_140_316_553_04, within(GEOTOOLS_BASELINE_TOLERANCE_DEGREES));
    assertThat(transformed.getY())
        .isCloseTo(59.998_005_924_722_655, within(GEOTOOLS_BASELINE_TOLERANCE_DEGREES));
    assertThat(transformed.getSRID()).isEqualTo(GeometryReprojectionUtils.SRID_4326);
  }

  @Test
  void to3005PreservesPolygonShellAndHole() {
    Polygon source =
        geometryFactory.createPolygon(
            geometryFactory.createLinearRing(
                new Coordinate[] {
                  new Coordinate(-123.40, 48.40),
                  new Coordinate(-123.40, 48.45),
                  new Coordinate(-123.35, 48.45),
                  new Coordinate(-123.35, 48.40),
                  new Coordinate(-123.40, 48.40)
                }),
            new LinearRing[] {
              geometryFactory.createLinearRing(
                  new Coordinate[] {
                    new Coordinate(-123.39, 48.41),
                    new Coordinate(-123.39, 48.42),
                    new Coordinate(-123.38, 48.42),
                    new Coordinate(-123.38, 48.41),
                    new Coordinate(-123.39, 48.41)
                  })
            });

    Geometry transformed = GeometryReprojectionUtils.to3005(source);

    assertThat((Object) transformed).isInstanceOf(Polygon.class);
    assertThat(((Polygon) transformed).getNumInteriorRing()).isEqualTo(1);
    assertThat(transformed.getNumPoints()).isEqualTo(source.getNumPoints());
    assertThat(transformed.getSRID()).isEqualTo(GeometryReprojectionUtils.SRID_3005);
  }

  @Test
  void roundTripPreservesMultiPolygonStructureCoordinatesAndInput() {
    Polygon first =
        geometryFactory.createPolygon(
            new Coordinate[] {
              new Coordinate(-123.40, 48.40, 10),
              new Coordinate(-123.40, 48.41, 11),
              new Coordinate(-123.39, 48.41, 12),
              new Coordinate(-123.40, 48.40, 10)
            });
    Polygon second =
        geometryFactory.createPolygon(
            new Coordinate[] {
              new Coordinate(-122.80, 53.90, 20),
              new Coordinate(-122.80, 53.91, 21),
              new Coordinate(-122.79, 53.91, 22),
              new Coordinate(-122.80, 53.90, 20)
            });
    MultiPolygon source = geometryFactory.createMultiPolygon(new Polygon[] {first, second});
    source.setSRID(GeometryReprojectionUtils.SRID_4326);
    Geometry original = source.copy();

    Geometry projected = GeometryReprojectionUtils.to3005(source);
    Geometry roundTrip = GeometryReprojectionUtils.to4326(projected);

    assertThat((Object) projected).isInstanceOf(MultiPolygon.class);
    assertThat(projected.getNumGeometries()).isEqualTo(2);
    assertThat(projected.getSRID()).isEqualTo(GeometryReprojectionUtils.SRID_3005);
    assertThat((Object) roundTrip).isInstanceOf(MultiPolygon.class);
    assertThat(roundTrip.getSRID()).isEqualTo(GeometryReprojectionUtils.SRID_4326);
    assertThat(roundTrip.equalsExact(source, ROUND_TRIP_TOLERANCE_DEGREES)).isTrue();
    assertThat(roundTrip.getCoordinates()[1].getZ()).isEqualTo(11);
    assertThat(source.equalsExact(original)).isTrue();
  }
}
