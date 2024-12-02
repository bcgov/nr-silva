package ca.bc.gov.restapi.results.common.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Unit Test | YesNoEnum")
class YesNoEnumTest {

  @DisplayName("Test enum conversion")
  @MethodSource("enumConversion")
  @ParameterizedTest(name = "Test enum conversion for code {0} and expected {1}")
  void testEnumConversion(String code, YesNoEnum expected) {
    YesNoEnum actual = YesNoEnum.fromValue(code);
    assertEquals(expected, actual);
    if (expected != null) {
      assertEquals(expected.value(), actual.value());
    }
  }


  private static Stream<Arguments> enumConversion() {
    return Stream.of(
        Arguments.of("Y", YesNoEnum.YES),
        Arguments.of("N", YesNoEnum.NO),
        Arguments.of(null, null),
        Arguments.of("J", null)
    );
  }

}