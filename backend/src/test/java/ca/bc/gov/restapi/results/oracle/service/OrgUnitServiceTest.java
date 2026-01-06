package ca.bc.gov.restapi.results.oracle.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.repository.OrgUnitRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Org Unit Service")
class OrgUnitServiceTest {

  @Mock OrgUnitRepository orgUnitRepository;

  private OrgUnitService orgUnitService;
  private final SilvaConfiguration silvaConfiguration = new SilvaConfiguration().withOrgUnits(List.of("DAS"));

  @BeforeEach
  void setup() {
    orgUnitService = new OrgUnitService(orgUnitRepository, silvaConfiguration);
  }

  @Test
  @DisplayName("Find all org units happy path should succeed")
  void findAllOrgUnits_happyPath_shouldSucceed() {
    OrgUnitEntity orgUnit = new OrgUnitEntity();
    orgUnit.setOrgUnitNo(22L);
    orgUnit.setOrgUnitCode("DAS");
    orgUnit.setOrgUnitName("DAS Name");
    orgUnit.setLocationCode("123");
    orgUnit.setAreaCode("1");
    orgUnit.setTelephoneNo("25436521");
    orgUnit.setOrgLevelCode('R');
    orgUnit.setOfficeNameCode("RR");
    orgUnit.setRollupRegionNo(12L);
    orgUnit.setRollupRegionCode("19");
    orgUnit.setRollupDistNo(13L);
    orgUnit.setRollupDistCode("25");
    orgUnit.setEffectiveDate(LocalDate.now().minusYears(3L));
    orgUnit.setExpiryDate(LocalDate.now().plusYears(3L));
    orgUnit.setUpdateTimestamp(LocalDate.now());

    when(orgUnitRepository.findAllByOrgUnitCodeIn(List.of("DAS")))
        .thenReturn(List.of(orgUnit));
    List<CodeDescriptionDto> entities = orgUnitService.findAllOrgUnits();

    Assertions.assertNotNull(entities);
    Assertions.assertEquals(1, entities.size());

    CodeDescriptionDto responseOrg = entities.get(0);
    Assertions.assertEquals("DAS", responseOrg.code());
    Assertions.assertEquals("DAS Name", responseOrg.description());

    verify(orgUnitRepository, times(0)).findAll();
  }

  @Test
  @DisplayName("Find all org units empty response should succeed")
  void findAllOrgUnits_emptyResponse_shouldSucceed() {
    orgUnitService = new OrgUnitService(orgUnitRepository, silvaConfiguration);

    when(orgUnitRepository.findAllByOrgUnitCodeIn(List.of("DAS"))).thenReturn(List.of());
    List<CodeDescriptionDto> entities = orgUnitService.findAllOrgUnits();

    Assertions.assertNotNull(entities);
    Assertions.assertTrue(entities.isEmpty());

    verify(orgUnitRepository, times(0)).findAll();
  }

  @Test
  @DisplayName("Find all org units empty response should succeed")
  void findNoOrgUnits_emptyResponse_shouldSucceed() {
    orgUnitService = new OrgUnitService(orgUnitRepository, silvaConfiguration.withOrgUnits(List.of()));

    List<CodeDescriptionDto> entities = orgUnitService.findAllOrgUnits();

    Assertions.assertNotNull(entities);
    Assertions.assertTrue(entities.isEmpty());

    verify(orgUnitRepository, times(0)).findAll();
  }

}
