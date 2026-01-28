package ca.bc.gov.restapi.results.oracle.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import ca.bc.gov.restapi.results.common.enums.OpeningCategoryEnum;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Unit Test | OpeningCategoryEnum")
class OpeningCategoryEnumTest {

  @DisplayName("Test enum conversion")
  @ParameterizedTest(name = "Test enum conversion for code {0} and description {1}")
  @MethodSource("enumConversion")
  void testEnumConversion(String code, String description, OpeningCategoryEnum expected) {
    OpeningCategoryEnum actual = OpeningCategoryEnum.of(code);
    assertEquals(expected, actual);
    if (expected != null) {
      assertEquals(expected.getCode(), actual.getCode());
      assertEquals(description, actual.getDescription());
    }
  }

  private static Stream<Arguments> enumConversion() {
    return Stream.of(
        Arguments.of("CONT", "SP as a part of contractual agreement", OpeningCategoryEnum.CONT),
        Arguments.of("EXCLU", "Openings excluded from Crown managed forests",
            OpeningCategoryEnum.EXCLU),
        Arguments.of("FFTFLTC", "Forest For Tomorrow - Forestry Licence to Cut",
            OpeningCategoryEnum.FFTFLTC),
        Arguments.of("FFTITSL", "Forest For Tomorrow - Innovative Timber Sale Licence",
            OpeningCategoryEnum.FFTITSL),
        Arguments.of("FTCF", "Forest Tenure - Site Plan under Community Forest",
            OpeningCategoryEnum.FTCF),
        Arguments.of("FTFSM", "Forest Tenure - Forest Stand Management FPC s.71",
            OpeningCategoryEnum.FTFSM),
        Arguments.of("FTLEVY", "Forest Stand Levy under FSM Fund Reg.", OpeningCategoryEnum.FTLEVY),
        Arguments.of("FTML", "Forest Tenure - Major Licensee", OpeningCategoryEnum.FTML),
        Arguments.of("FTMSL", "Forest Tenure Ministry Silviculture Liability",
            OpeningCategoryEnum.FTMSL),
        Arguments.of(
            "FTNOLVY",
            "Post April 1/09 blocks (1-5 ha) for which there is no opportunity within the appraisal"
            + " manual to collect a levy.",
            OpeningCategoryEnum.FTNOLVY),
        Arguments.of("FTPI", "Forest Tenure - pilot agreement", OpeningCategoryEnum.FTPI),
        Arguments.of("FTSBF", "Forest Tenure - Small Business Forest Enterprise Program",
            OpeningCategoryEnum.FTSBF),
        Arguments.of("FTWL", "Forest Tenure - Woodlot: Site Plan", OpeningCategoryEnum.FTWL),
        Arguments.of("NDAML", "Natural Disturbance - area-based Major Licensee",
            OpeningCategoryEnum.NDAML),
        Arguments.of("NDCF", "Natural Disturbance - Community Forest", OpeningCategoryEnum.NDCF),
        Arguments.of("NDVML", "Natural Disturbance - volume-based Major Licensee",
            OpeningCategoryEnum.NDVML),
        Arguments.of("NDWL", "Natural Disturbance - Woodlot License", OpeningCategoryEnum.NDWL),
        Arguments.of("NREQ", "Areas where SP/SMP's are not required by law",
            OpeningCategoryEnum.NREQ),
        Arguments.of(
            "SPEX",
            "Areas with no reforestation or FG obligations and the area is exempt from a site plan"
            + " requirement",
            OpeningCategoryEnum.SPEX),
        Arguments.of("UHRV", "Unauthorized Harvesting", OpeningCategoryEnum.UHRV),
        Arguments.of(StringUtils.EMPTY, null, null),
        Arguments.of(null, null, null),
        Arguments.of(" ", null, null),
        Arguments.of("ABC", null, null)
    );
  }
}
