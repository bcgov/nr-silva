package ca.bc.gov.restapi.results.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OpeningStatusEnumTest {

  @Test
  void getByCodeTest() {
    String code = "APP";
    OpeningStatusEnum app = OpeningStatusEnum.of(code);

    Assertions.assertNotNull(app);
    Assertions.assertEquals(app, OpeningStatusEnum.APP);
    Assertions.assertEquals(code, app.getCode());
    Assertions.assertFalse(app.getDescription().isBlank());
  }

  @Test
  void getByNonExistingCodeTest() {
    Assertions.assertNull(OpeningStatusEnum.of("RESULTS"));
  }
}
