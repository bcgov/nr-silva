package ca.bc.gov.restapi.results.postgres;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaPostgresConstants {

  public static final Map<Integer, String> LABELS_MAP = Map.of(
      0, "0 - 5 months",
      1, "6 - 11 months",
      2, "12 - 17 months",
      3, "18 months"
  );
}
