package ca.bc.gov.restapi.results.common.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | ExternalCutBlockDetails")
class ExternalCutBlockDetailsTest {

  @Test
  @DisplayName("notFound factory should return record with exists=false and null fields")
  void notFound_createsExpectedRecord() {
    ExternalCutBlockDetails details = ExternalCutBlockDetails.notFound();

    assertThat(details.exists()).isFalse();
    assertThat(details.cbSkey()).isNull();
    assertThat(details.clientIsLicensee()).isFalse();
    assertThat(details.timberMark()).isNull();
    assertThat(details.status()).isNull();
  }

  @Test
  @DisplayName("of factory should return record with exists=true and supplied fields")
  void of_createsExpectedRecord() {
    ExternalCutBlockDetails details = ExternalCutBlockDetails.of(12345L, true, "TM100", "ACT");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(12345L);
    assertThat(details.clientIsLicensee()).isTrue();
    assertThat(details.timberMark()).isEqualTo("TM100");
    assertThat(details.status()).isEqualTo("ACT");
  }

  @Test
  @DisplayName("Canonical constructor should populate all fields")
  void canonicalConstructor_populatesAllFields() {
    ExternalCutBlockDetails details =
        new ExternalCutBlockDetails(true, 9999L, false, "47/12K", "LOG");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(9999L);
    assertThat(details.clientIsLicensee()).isFalse();
    assertThat(details.timberMark()).isEqualTo("47/12K");
    assertThat(details.status()).isEqualTo("LOG");
  }

  @Test
  @DisplayName("Equals and hashCode should adhere to record value semantics")
  void equalsAndHashCode_valueSemantics() {
    ExternalCutBlockDetails details1 =
        new ExternalCutBlockDetails(true, 1001L, true, "TM1", "ACT");
    ExternalCutBlockDetails details2 =
        ExternalCutBlockDetails.of(1001L, true, "TM1", "ACT");
    ExternalCutBlockDetails details3 =
        new ExternalCutBlockDetails(true, 1002L, true, "TM1", "ACT");

    assertThat(details1).isEqualTo(details2);
    assertThat(details1).hasSameHashCodeAs(details2);
    assertThat(details1).isNotEqualTo(details3);
  }

  @Test
  @DisplayName("toString should contain class name and field values")
  void toString_containsFieldValues() {
    ExternalCutBlockDetails details = ExternalCutBlockDetails.of(1001L, true, "TM1", "ACT");

    assertThat(details.toString())
        .contains("ExternalCutBlockDetails")
        .contains("1001")
        .contains("TM1")
        .contains("ACT");
  }
}

