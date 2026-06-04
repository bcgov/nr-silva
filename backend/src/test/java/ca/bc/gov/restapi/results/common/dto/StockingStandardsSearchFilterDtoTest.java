package ca.bc.gov.restapi.results.common.dto;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | StockingStandardsSearchFilterDto")
class StockingStandardsSearchFilterDtoTest {

  // -------------------------------------------------------------------------
  // No-arg constructor defaults
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("No-arg constructor sets lists to NOVALUE sentinel")
  void noArgConstructor_listsSentinelNoValue() {
    StockingStandardsSearchFilterDto dto = new StockingStandardsSearchFilterDto();
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getPreferredSpecies());
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getOrgUnits());
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getClientNumbers());
  }

  @Test
  @DisplayName("No-arg constructor sets all string fields to null")
  void noArgConstructor_stringFieldsNull() {
    StockingStandardsSearchFilterDto dto = new StockingStandardsSearchFilterDto();
    Assertions.assertNull(dto.getStandardsRegimeId());
    Assertions.assertNull(dto.getFspId());
    Assertions.assertNull(dto.getBgcZone());
    Assertions.assertNull(dto.getBgcSubZone());
    Assertions.assertNull(dto.getBgcVariant());
    Assertions.assertNull(dto.getBgcPhase());
    Assertions.assertNull(dto.getBecSiteSeries());
    Assertions.assertNull(dto.getBecSiteType());
    Assertions.assertNull(dto.getBecSeral());
    Assertions.assertNull(dto.getApprovedDateStart());
    Assertions.assertNull(dto.getApprovedDateEnd());
  }

  // -------------------------------------------------------------------------
  // List field normalization
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("Non-empty list is stored as uppercase")
  void listFields_storedAsUpperCase() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null,
            List.of("cw", "Fd"),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    Assertions.assertEquals(List.of("CW", "FD"), dto.getPreferredSpecies());
  }

  @Test
  @DisplayName("Null list defaults to NOVALUE sentinel")
  void nullList_defaultsToNoValue() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null);
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getPreferredSpecies());
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getOrgUnits());
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getClientNumbers());
  }

  @Test
  @DisplayName("Empty list defaults to NOVALUE sentinel")
  void emptyList_defaultsToNoValue() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null, List.of(), List.of(), List.of(), null, null, null, null, null, null, null, null,
            null, null,
            null);
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getPreferredSpecies());
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getOrgUnits());
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getClientNumbers());
  }

  // -------------------------------------------------------------------------
  // String field normalization
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("bgcZone is trimmed and uppercased")
  void bgcZone_trimmedAndUppercased() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, "  cwh  ", null, null, null, null, null, null, null,
            null,
            null);
    Assertions.assertEquals("CWH", dto.getBgcZone());
  }

  @Test
  @DisplayName("Blank bgcZone is stored as null")
  void bgcZone_blank_storedAsNull() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, "   ", null, null, null, null, null, null, null, null,
            null);
    Assertions.assertNull(dto.getBgcZone());
  }

  @Test
  @DisplayName("bgcSubZone is trimmed and uppercased")
  void bgcSubZone_trimmedAndUppercased() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, null, " wm ", null, null, null, null, null, null, null,
            null);
    Assertions.assertEquals("WM", dto.getBgcSubZone());
  }

  @Test
  @DisplayName("becSeral is trimmed and uppercased")
  void becSeral_trimmedAndUppercased() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, null, null, null, null, null, null, " cl ", null, null,
            null);
    Assertions.assertEquals("CL", dto.getBecSeral());
  }

  @Test
  @DisplayName("fspId is trimmed only (not uppercased)")
  void fspId_trimmedOnly() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null,
            null,
            null,
            null,
            "  fsp123  ",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    Assertions.assertEquals("fsp123", dto.getFspId());
  }

  @Test
  @DisplayName("Blank fspId is stored as null")
  void fspId_blank_storedAsNull() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, "   ", null, null, null, null, null, null, null, null, null,
            null);
    Assertions.assertNull(dto.getFspId());
  }

  @Test
  @DisplayName("becSiteSeries is trimmed only (not uppercased)")
  void becSiteSeries_trimmedOnly() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, null, null, null, null, " 01 ", null, null, null, null,
            null);
    Assertions.assertEquals("01", dto.getBecSiteSeries());
  }

  @Test
  @DisplayName("approvedDateStart is trimmed; blank becomes null")
  void approvedDateStart_trimmedAndBlankIsNull() {
    StockingStandardsSearchFilterDto dtoNonBlank =
        new StockingStandardsSearchFilterDto(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            " 2024-01-01 ",
            null,
            null);
    Assertions.assertEquals("2024-01-01", dtoNonBlank.getApprovedDateStart());

    StockingStandardsSearchFilterDto dtoBlank =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, null, null, null, null, null, null, null, "   ", null,
            null);
    Assertions.assertNull(dtoBlank.getApprovedDateStart());
  }

  // -------------------------------------------------------------------------
  // hasAnyFilter
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("hasAnyFilter returns false for all-null / all-defaults")
  void hasAnyFilter_allDefaults_returnsFalse() {
    Assertions.assertFalse(new StockingStandardsSearchFilterDto().hasAnyFilter());
  }

  @Test
  @DisplayName("hasAnyFilter returns true when standardsRegimeId is set")
  void hasAnyFilter_regimeId_returnsTrue() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            42L, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null);
    Assertions.assertTrue(dto.hasAnyFilter());
  }

  @Test
  @DisplayName("hasAnyFilter returns true when preferredSpecies is set")
  void hasAnyFilter_preferredSpecies_returnsTrue() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null,
            List.of("CW"),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    Assertions.assertTrue(dto.hasAnyFilter());
  }

  @Test
  @DisplayName("hasAnyFilter returns true when bgcZone is set")
  void hasAnyFilter_bgcZone_returnsTrue() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, "CWH", null, null, null, null, null, null, null, null,
            null);
    Assertions.assertTrue(dto.hasAnyFilter());
  }

  @Test
  @DisplayName("hasAnyFilter returns true when approvedDateStart is set")
  void hasAnyFilter_approvedDateStart_returnsTrue() {
    StockingStandardsSearchFilterDto dto =
        new StockingStandardsSearchFilterDto(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "2024-01-01",
            null,
            null);
    Assertions.assertTrue(dto.hasAnyFilter());
  }
}
