package ca.bc.gov.restapi.results.oracle.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import ca.bc.gov.restapi.results.common.enums.OpeningStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Unit Test | OpeningStatusEnum")
class OpeningStatusEnumTest {

  @DisplayName("Test enum conversion")
  @ParameterizedTest(name = "Test enum conversion for code {0} and description {1}")
  @MethodSource("enumConversion")
  void testEnumConversion(String code, String description, OpeningStatusEnum expected) {
    OpeningStatusEnum actual = OpeningStatusEnum.of(code);
    assertEquals(expected, actual);
    if (expected != null) {
      assertEquals(expected.getCode(), actual.getCode());
      assertEquals(description, actual.getDescription());
    }
  }

  private static Stream<Arguments> enumConversion() {
    return Stream.of(
        Arguments.of("AMD", "Amended", OpeningStatusEnum.AMD),
        Arguments.of("AMG", "Amalgamate", OpeningStatusEnum.AMG),
        Arguments.of("APP", "Approved", OpeningStatusEnum.APP),
        Arguments.of("DFT", "Draft", OpeningStatusEnum.DFT),
        Arguments.of("FG", "Free Growing", OpeningStatusEnum.FG),
        Arguments.of("RET", "Retired", OpeningStatusEnum.RET),
        Arguments.of("SUB", "Submitted", OpeningStatusEnum.SUB),
        Arguments.of(StringUtils.EMPTY, null, null),
        Arguments.of(null, null, null),
        Arguments.of(" ", null, null),
        Arguments.of("ABC", null, null)
    );
  }
}
