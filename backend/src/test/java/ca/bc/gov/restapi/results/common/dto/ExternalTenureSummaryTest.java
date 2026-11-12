package ca.bc.gov.restapi.results.common.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | ExternalTenureSummary")
class ExternalTenureSummaryTest {

  @Test
  @DisplayName("6-argument constructor sets cbSkey to null and populates other fields")
  void sixArgConstructor_populatesFieldsWithNullCbSkey() {
    ExternalTenureSummary summary =
        new ExternalTenureSummary("TFL001", "CP1", "CB001", "47/12K", "00010003", "ACT");

    assertThat(summary.forestFileId()).isEqualTo("TFL001");
    assertThat(summary.cuttingPermitId()).isEqualTo("CP1");
    assertThat(summary.cutBlockId()).isEqualTo("CB001");
    assertThat(summary.timberMark()).isEqualTo("47/12K");
    assertThat(summary.licenseeClientNumber()).isEqualTo("00010003");
    assertThat(summary.status()).isEqualTo("ACT");
    assertThat(summary.cbSkey()).isNull();
  }

  @Test
  @DisplayName("7-argument constructor populates all fields including cbSkey")
  void sevenArgConstructor_populatesAllFields() {
    ExternalTenureSummary summary =
        new ExternalTenureSummary("TFL001", "CP1", "CB001", "47/12K", "00010003", "ACT", 1001L);

    assertThat(summary.forestFileId()).isEqualTo("TFL001");
    assertThat(summary.cuttingPermitId()).isEqualTo("CP1");
    assertThat(summary.cutBlockId()).isEqualTo("CB001");
    assertThat(summary.timberMark()).isEqualTo("47/12K");
    assertThat(summary.licenseeClientNumber()).isEqualTo("00010003");
    assertThat(summary.status()).isEqualTo("ACT");
    assertThat(summary.cbSkey()).isEqualTo(1001L);
  }

  @Test
  @DisplayName("Equals and hashCode should adhere to record value semantics")
  void equalsAndHashCode_valueSemantics() {
    ExternalTenureSummary summary1 =
        new ExternalTenureSummary("TFL001", "CP1", "CB001", "47/12K", "00010003", "ACT", 1001L);
    ExternalTenureSummary summary2 =
        new ExternalTenureSummary("TFL001", "CP1", "CB001", "47/12K", "00010003", "ACT", 1001L);
    ExternalTenureSummary summary3 =
        new ExternalTenureSummary("TFL001", "CP1", "CB001", "47/12K", "00010003", "ACT");

    assertThat(summary1).isEqualTo(summary2);
    assertThat(summary1).hasSameHashCodeAs(summary2);
    assertThat(summary1).isNotEqualTo(summary3);
  }

  @Test
  @DisplayName("toString should contain class name and field values")
  void toString_containsFieldValues() {
    ExternalTenureSummary summary =
        new ExternalTenureSummary("TFL001", "CP1", "CB001", "47/12K", "00010003", "ACT", 1001L);

    assertThat(summary.toString())
        .contains("ExternalTenureSummary")
        .contains("TFL001")
        .contains("CP1")
        .contains("CB001")
        .contains("1001");
  }
}

