package ca.bc.gov.restapi.results.common.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Unit Test | ForestClientStatusEnum")
class ForestClientStatusEnumTest {


  @DisplayName("Test enum conversion")
  @ParameterizedTest(name = "Test enum conversion for code {0} and description {1}")
  @MethodSource("enumConversion")
  void testEnumConversion(String code, String description, ForestClientStatusEnum expected) {
    ForestClientStatusEnum actual = ForestClientStatusEnum.of(code);
    assertEquals(expected, actual);
    if (expected != null) {
      assertEquals(expected.getCode(), actual.getCode());
      assertEquals(description, actual.getDescription());
    }
  }

  private static Stream<Arguments> enumConversion() {
    return Stream.of(
        Arguments.of("ACT", "Active", ForestClientStatusEnum.ACTIVE),
        Arguments.of("DAC", "Deactivated", ForestClientStatusEnum.DEACTIVATED),
        Arguments.of("DEC", "Deceased", ForestClientStatusEnum.DECEASED),
        Arguments.of("REC", "Receivership", ForestClientStatusEnum.RECEIVERSHIP),
        Arguments.of("SPN", "Suspended", ForestClientStatusEnum.SUSPENDED),
        Arguments.of(StringUtils.EMPTY, null, null),
        Arguments.of(null, null, null),
        Arguments.of(" ", null, null),
        Arguments.of("ABC", null, null)
    );
  }

}