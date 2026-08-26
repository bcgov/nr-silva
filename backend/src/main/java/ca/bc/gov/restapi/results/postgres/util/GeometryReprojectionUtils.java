package ca.bc.gov.restapi.results.postgres.util;

import java.util.Objects;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;

/** Native-safe reprojection for the coordinate systems supported by opening spatial files. */
public final class GeometryReprojectionUtils {

  public static final int SRID_3005 = 3005;
  public static final int SRID_4326 = 4326;

  private static final CRSFactory CRS_FACTORY = new CRSFactory();
  private static final CoordinateTransformFactory TRANSFORM_FACTORY =
      new CoordinateTransformFactory();

  private static final CoordinateReferenceSystem CRS_3005 =
      CRS_FACTORY.createFromParameters(
          "EPSG:3005",
          "+proj=aea +lat_1=50 +lat_2=58.5 +lat_0=45 +lon_0=-126 "
              + "+x_0=1000000 +y_0=0 +datum=NAD83 +units=m +no_defs");
  private static final CoordinateReferenceSystem CRS_4326 =
      CRS_FACTORY.createFromParameters("EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");

  private static final CoordinateTransform TO_3005 =
      TRANSFORM_FACTORY.createTransform(CRS_4326, CRS_3005);
  private static final CoordinateTransform TO_4326 =
      TRANSFORM_FACTORY.createTransform(CRS_3005, CRS_4326);

  private GeometryReprojectionUtils() {}

  public static Geometry to3005(Geometry geometry4326) {
    return transform(geometry4326, TO_3005, SRID_3005);
  }

  public static Geometry to4326(Geometry geometry3005) {
    return transform(geometry3005, TO_4326, SRID_4326);
  }

  private static Geometry transform(
      Geometry sourceGeometry, CoordinateTransform coordinateTransform, int targetSrid) {
    Objects.requireNonNull(sourceGeometry, "sourceGeometry must not be null");

    Geometry transformedGeometry = sourceGeometry.copy();
    transformedGeometry.apply(
        new CoordinateSequenceFilter() {
          @Override
          public void filter(CoordinateSequence sequence, int index) {
            ProjCoordinate source = new ProjCoordinate(sequence.getX(index), sequence.getY(index));
            ProjCoordinate target = new ProjCoordinate();
            coordinateTransform.transform(source, target);
            sequence.setOrdinate(index, 0, target.x);
            sequence.setOrdinate(index, 1, target.y);
          }

          @Override
          public boolean isDone() {
            return false;
          }

          @Override
          public boolean isGeometryChanged() {
            return true;
          }
        });
    transformedGeometry.setSRID(targetSrid);
    return transformedGeometry;
  }
}
