package ca.bc.gov.restapi.results.common.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Unit Test | ForestClientTypeEnum")
class ForestClientTypeEnumTest {


  @DisplayName("Test enum conversion")
  @ParameterizedTest(name = "Test enum conversion for code {0} and description {1}")
  @MethodSource("enumConversion")
  void testEnumConversion(Character code, String description, ForestClientTypeEnum expected) {
    ForestClientTypeEnum actual = ForestClientTypeEnum.of(code);
    assertEquals(expected, actual);
    if (expected != null) {
      assertEquals(expected.getCode(), actual.getCode());
      assertEquals(description, actual.getDescription());
    }
  }

  private static Stream<Arguments> enumConversion() {
    return Stream.of(
        Arguments.of('A', "Association", ForestClientTypeEnum.ASSOCIATION),
        Arguments.of('B', "First Nation Band", ForestClientTypeEnum.FIRST_NATION_BAND),
        Arguments.of('C', "Corporation", ForestClientTypeEnum.CORPORATION),
        Arguments.of('F', "Ministry of Forests and Range", ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE),
        Arguments.of('G', "Government", ForestClientTypeEnum.GOVERNMENT),
        Arguments.of('I', "Individual", ForestClientTypeEnum.INDIVIDUAL),
        Arguments.of('L', "Limited Partnership", ForestClientTypeEnum.LIMITED_PARTNERSHIP),
        Arguments.of('P', "General Partnership", ForestClientTypeEnum.GENERAL_PARTNERSHIP),
        Arguments.of('R', "First Nation Group", ForestClientTypeEnum.FIRST_NATION_GROUP),
        Arguments.of('S', "Society", ForestClientTypeEnum.SOCIETY),
        Arguments.of('T', "First Nation Tribal Council", ForestClientTypeEnum.FIRST_NATION_TRIBAL_COUNCIL),
        Arguments.of('U', "Unregistered Company", ForestClientTypeEnum.UNREGISTERED_COMPANY),
        Arguments.of(null, null, null),
        Arguments.of('J', null, null)
    );
  }

}