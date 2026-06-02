package ca.bc.gov.restapi.results.common.dto.stockingstandards;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.enums.StockingStandardsCommentLocationCode;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | StockingStandardsCommentSearchFilterDto")
class StockingStandardsCommentSearchFilterDtoTest {

  // -------------------------------------------------------------------------
  // clientNumbers — null / empty → NOVALUE sentinel; non-empty → uppercase
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("clientNumbers is NOVALUE sentinel when null")
  void clientNumbers_nullInput_isNoValueSentinel() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto("term", null, null, null, null, null);
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getClientNumbers());
  }

  @Test
  @DisplayName("clientNumbers is NOVALUE sentinel when empty list")
  void clientNumbers_emptyInput_isNoValueSentinel() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto("term", null, List.of(), null, null, null);
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getClientNumbers());
  }

  @Test
  @DisplayName("clientNumbers stored as uppercase")
  void clientNumbers_storedAsUppercase() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto(
            "term", null, List.of("00099001"), null, null, null);
    Assertions.assertEquals(List.of("00099001"), dto.getClientNumbers());
  }

  // -------------------------------------------------------------------------
  // orgUnits — null / empty → NOVALUE sentinel; non-empty → uppercase
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("orgUnits is NOVALUE sentinel when null")
  void orgUnits_nullInput_isNoValueSentinel() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto("term", null, null, null, null, null);
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getOrgUnits());
  }

  @Test
  @DisplayName("orgUnits is NOVALUE sentinel when empty list")
  void orgUnits_emptyInput_isNoValueSentinel() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto("term", null, null, List.of(), null, null);
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getOrgUnits());
  }

  @Test
  @DisplayName("orgUnits stored as uppercase")
  void orgUnits_storedAsUppercase() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto("term", null, null, List.of("das"), null, null);
    Assertions.assertEquals(List.of("DAS"), dto.getOrgUnits());
  }

  // -------------------------------------------------------------------------
  // getCommentLocationValues
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("getCommentLocationValues returns NOVALUE when commentLocations is null")
  void getCommentLocationValues_null_returnsNoValue() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto("term", null, null, null, null, null);
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getCommentLocationValues());
  }

  @Test
  @DisplayName("getCommentLocationValues returns NOVALUE when commentLocations is empty")
  void getCommentLocationValues_empty_returnsNoValue() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto("term", List.of(), null, null, null, null);
    Assertions.assertEquals(List.of(SilvaConstants.NOVALUE), dto.getCommentLocationValues());
  }

  @Test
  @DisplayName("getCommentLocationValues returns enum name for single location")
  void getCommentLocationValues_singleLocation_returnsEnumName() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto(
            "term",
            List.of(StockingStandardsCommentLocationCode.STANDARDS_NAME),
            null,
            null,
            null,
            null);
    Assertions.assertEquals(List.of("STANDARDS_NAME"), dto.getCommentLocationValues());
  }

  @Test
  @DisplayName("getCommentLocationValues returns all enum names for multiple locations")
  void getCommentLocationValues_multipleLocations_returnsAllNames() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto(
            "term",
            List.of(
                StockingStandardsCommentLocationCode.STANDARDS_NAME,
                StockingStandardsCommentLocationCode.ADDITIONAL_STANDARDS,
                StockingStandardsCommentLocationCode.STANDARDS_OBJECTIVE),
            null,
            null,
            null,
            null);
    Assertions.assertEquals(
        List.of("STANDARDS_NAME", "ADDITIONAL_STANDARDS", "STANDARDS_OBJECTIVE"),
        dto.getCommentLocationValues());
  }

  // -------------------------------------------------------------------------
  // Passthrough fields
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("searchTerm is stored as-is")
  void searchTerm_storedAsIs() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto("Baseline", null, null, null, null, null);
    Assertions.assertEquals("Baseline", dto.getSearchTerm());
  }

  @Test
  @DisplayName("date fields stored as-is")
  void dateFields_storedAsIs() {
    StockingStandardsCommentSearchFilterDto dto =
        new StockingStandardsCommentSearchFilterDto(
            "term", null, null, null, "2020-01-01", "2025-12-31");
    Assertions.assertEquals("2020-01-01", dto.getUpdateDateStart());
    Assertions.assertEquals("2025-12-31", dto.getUpdateDateEnd());
  }
}
