package ca.bc.gov.restapi.results.oracle.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import ca.bc.gov.restapi.results.common.enums.AuditActionCodeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Unit Test | AuditActionCodeEnum")
class AuditActionCodeEnumTest {

  @DisplayName("Test enum conversion")
  @ParameterizedTest(name = "Test enum conversion for code {0} and description {1}")
  @MethodSource("enumConversion")
  void testEnumConversion(String code, String description, AuditActionCodeEnum expected) {
    AuditActionCodeEnum actual = AuditActionCodeEnum.of(code);
    assertEquals(expected, actual);
    if (expected != null) {
      assertEquals(expected.getCode(), actual.getCode());
      assertEquals(description, actual.getDescription());
    }
  }

  private static Stream<Arguments> enumConversion() {
    return Stream.of(
        Arguments.of("UPD", "Update", AuditActionCodeEnum.UPD),
        Arguments.of("COR", "Correction", AuditActionCodeEnum.COR),
        Arguments.of("O", "Original", AuditActionCodeEnum.O),
        Arguments.of("197", "Section 197", AuditActionCodeEnum.SEC197),
        Arguments.of("AMG", "Amalgamate", AuditActionCodeEnum.AMG),
        Arguments.of("ES", "E-submission", AuditActionCodeEnum.ES),
        Arguments.of("MIL", "Milestone", AuditActionCodeEnum.MIL),
        Arguments.of("MIN", "Amended (Minor)", AuditActionCodeEnum.MIN),
        Arguments.of("SPA", "Site Plan Amendment", AuditActionCodeEnum.SPA),
        Arguments.of("VAR", "Variation", AuditActionCodeEnum.VAR),
        Arguments.of("AMD", "Amended", AuditActionCodeEnum.AMD),
        Arguments.of("APP", "Approved", AuditActionCodeEnum.APP),
        Arguments.of("DEL", "Deleted", AuditActionCodeEnum.DEL),
        Arguments.of("REJ", "Rejected", AuditActionCodeEnum.REJ),
        Arguments.of("RET", "Retired", AuditActionCodeEnum.RET),
        Arguments.of("RMD", "Removed", AuditActionCodeEnum.RMD),
        Arguments.of("SUB", "Submitted", AuditActionCodeEnum.SUB),
        Arguments.of(null, null, null),
        Arguments.of("", null, null)
    );
  }

}
