package ca.bc.gov.restapi.results.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OpeningCategoryEnumTest {

  @Test
  void getByCodeTest() {
    String code = "FTML";
    OpeningCategoryEnum ftml = OpeningCategoryEnum.of(code);

    Assertions.assertNotNull(ftml);
    Assertions.assertEquals(ftml, OpeningCategoryEnum.FTML);
    Assertions.assertEquals(code, ftml.getCode());
    Assertions.assertFalse(ftml.getDescription().isBlank());
  }

  @Test
  void getByNonExistingCodeTest() {
    Assertions.assertNull(OpeningCategoryEnum.of("RESULTS"));
  }
}
