package ca.bc.gov.restapi.results.common.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.service.CodeService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | CodesEndpoint")
class CodesEndpointUnitTest {

  @Mock private CodeService codeService;

  private CodesEndpoint endpoint;

  @BeforeEach
  void setUp() {
    endpoint = new CodesEndpoint(codeService);
  }

  @Test
  @DisplayName("getSilvBaseCodes with effectiveOnly should delegate to codeService")
  void getSilvBaseCodes_shouldDelegate() {
    when(codeService.getAllSilvBaseCode(true)).thenReturn(List.of(new CodeDescriptionDto("BASE", "Desc")));
    List<CodeDescriptionDto> result = endpoint.getSilvBaseCodes(true);
    assertThat(result).hasSize(1);
    verify(codeService).getAllSilvBaseCode(true);

    endpoint.getSilvBaseCodes(false);
    endpoint.getSilvBaseCodes(null);
    verify(codeService, org.mockito.Mockito.times(2)).getAllSilvBaseCode(false);
  }

  @Test
  @DisplayName("getSilvTechniqueCodes with effectiveOnly should delegate to codeService")
  void getSilvTechniqueCodes_shouldDelegate() {
    endpoint.getSilvTechniqueCodes(true);
    verify(codeService).getAllSilvTechniqueCode(true);

    endpoint.getSilvTechniqueCodes(false);
    verify(codeService).getAllSilvTechniqueCode(false);
  }

  @Test
  @DisplayName("getSilvMethodCodes with effectiveOnly should delegate to codeService")
  void getSilvMethodCodes_shouldDelegate() {
    endpoint.getSilvMethodCodes(true);
    verify(codeService).getAllSilvMethodCode(true);

    endpoint.getSilvMethodCodes(false);
    verify(codeService).getAllSilvMethodCode(false);
  }

  @Test
  @DisplayName("getSilvObjectiveCodes with effectiveOnly should delegate to codeService")
  void getSilvObjectiveCodes_shouldDelegate() {
    endpoint.getSilvObjectiveCodes(true);
    verify(codeService).getAllSilvObjectiveCode(true);

    endpoint.getSilvObjectiveCodes(false);
    verify(codeService).getAllSilvObjectiveCode(false);
  }

  @Test
  @DisplayName("getSilvFundSourceCodes with effectiveOnly should delegate to codeService")
  void getSilvFundSourceCodes_shouldDelegate() {
    endpoint.getSilvFundSourceCodes(true);
    verify(codeService).getAllSilvFundSrceCode(true);

    endpoint.getSilvFundSourceCodes(false);
    verify(codeService).getAllSilvFundSrceCode(false);
  }

  @Test
  @DisplayName("getOpeningStatusCodes with effectiveOnly should delegate to codeService")
  void getOpeningStatusCodes_shouldDelegate() {
    endpoint.getOpeningStatusCodes(true);
    verify(codeService).getAllOpenStatusCode(true);

    endpoint.getOpeningStatusCodes(false);
    verify(codeService).getAllOpenStatusCode(false);
  }

  @Test
  @DisplayName("getSilvSystemCodes with effectiveOnly should delegate to codeService")
  void getSilvSystemCodes_shouldDelegate() {
    endpoint.getSilvSystemCodes(true);
    verify(codeService).getAllSilvSystemCode(true);

    endpoint.getSilvSystemCodes(false);
    verify(codeService).getAllSilvSystemCode(false);
  }

  @Test
  @DisplayName("getSilvSystemVariantCodes with effectiveOnly should delegate to codeService")
  void getSilvSystemVariantCodes_shouldDelegate() {
    endpoint.getSilvSystemVariantCodes(true);
    verify(codeService).getAllSilvSystemVariantCode(true);

    endpoint.getSilvSystemVariantCodes(false);
    verify(codeService).getAllSilvSystemVariantCode(false);
  }

  @Test
  @DisplayName("getSilvCutPhaseCodes with effectiveOnly should delegate to codeService")
  void getSilvCutPhaseCodes_shouldDelegate() {
    endpoint.getSilvCutPhaseCodes(true);
    verify(codeService).getAllSilvCutPhaseCode(true);

    endpoint.getSilvCutPhaseCodes(false);
    verify(codeService).getAllSilvCutPhaseCode(false);
  }

  @Test
  @DisplayName("getDisturbanceCodes with effectiveOnly should delegate to codeService")
  void getDisturbanceCodes_shouldDelegate() {
    endpoint.getDisturbanceCodes(true);
    verify(codeService).getAllDisturbanceCode(true);

    endpoint.getDisturbanceCodes(false);
    verify(codeService).getAllDisturbanceCode(false);
  }

  @Test
  @DisplayName("getSilvDamageAgentCodes with effectiveOnly should delegate to codeService")
  void getSilvDamageAgentCodes_shouldDelegate() {
    endpoint.getSilvDamageAgentCodes(true);
    verify(codeService).getAllSilvDamageAgentCode(true);

    endpoint.getSilvDamageAgentCodes(false);
    verify(codeService).getAllSilvDamageAgentCode(false);
  }

  @Test
  @DisplayName("getStockingStatusCodes with effectiveOnly should delegate to codeService")
  void getStockingStatusCodes_shouldDelegate() {
    endpoint.getStockingStatusCodes(true);
    verify(codeService).getAllStockingStatusCode(true);

    endpoint.getStockingStatusCodes(false);
    verify(codeService).getAllStockingStatusCode(false);
  }

  @Test
  @DisplayName("getStockingTypeCodes with effectiveOnly should delegate to codeService")
  void getStockingTypeCodes_shouldDelegate() {
    endpoint.getStockingTypeCodes(true);
    verify(codeService).getAllStockingTypeCode(true);

    endpoint.getStockingTypeCodes(false);
    verify(codeService).getAllStockingTypeCode(false);
  }

  @Test
  @DisplayName("getSilvTreeSpeciesCodes with effectiveOnly should delegate to codeService")
  void getSilvTreeSpeciesCodes_shouldDelegate() {
    endpoint.getSilvTreeSpeciesCodes(true);
    verify(codeService).getAllSilvTreeSpeciesCode(true);

    endpoint.getSilvTreeSpeciesCodes(false);
    verify(codeService).getAllSilvTreeSpeciesCode(false);
  }
}
