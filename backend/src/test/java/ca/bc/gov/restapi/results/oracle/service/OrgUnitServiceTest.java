package ca.bc.gov.restapi.results.oracle.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OrgUnitServiceTest {

  @Mock OrgUnitRepository orgUnitRepository;

  private OrgUnitService orgUnitService;

  @BeforeEach
  void setup() {
    orgUnitService = new OrgUnitService(orgUnitRepository, new String[] {"DAS"});
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

    when(orgUnitRepository.findAllByOrgUnitCodeIn(List.of("DAS"))).thenReturn(List.of(orgUnit));
    List<OrgUnitEntity> entities = orgUnitService.findAllOrgUnits();

    Assertions.assertNotNull(entities);
    Assertions.assertEquals(1, entities.size());

    OrgUnitEntity responseOrg = entities.get(0);
    Assertions.assertTrue(responseOrg.getExpiryDate().isAfter(LocalDate.now()));
    Assertions.assertEquals(22L, responseOrg.getOrgUnitNo());
    Assertions.assertEquals("DAS", responseOrg.getOrgUnitCode());
    Assertions.assertEquals("DAS Name", responseOrg.getOrgUnitName());
    Assertions.assertEquals("123", responseOrg.getLocationCode());
    Assertions.assertEquals("1", responseOrg.getAreaCode());
    Assertions.assertEquals("25436521", responseOrg.getTelephoneNo());
    Assertions.assertEquals('R', responseOrg.getOrgLevelCode());
    Assertions.assertEquals("RR", responseOrg.getOfficeNameCode());
    Assertions.assertEquals(12L, responseOrg.getRollupRegionNo());
    Assertions.assertEquals("19", responseOrg.getRollupRegionCode());
    Assertions.assertEquals(13L, responseOrg.getRollupDistNo());
    Assertions.assertEquals("25", responseOrg.getRollupDistCode());

    verify(orgUnitRepository, times(0)).findAll();
  }

  @Test
  @DisplayName("Find all org units empty response should succeed")
  void findAllOrgUnits_emptyResponse_shouldSucceed() {
    orgUnitService = new OrgUnitService(orgUnitRepository, new String[] {});

    when(orgUnitRepository.findAllByOrgUnitCodeIn(List.of("DAS"))).thenReturn(List.of());
    List<OrgUnitEntity> entities = orgUnitService.findAllOrgUnits();

    Assertions.assertNotNull(entities);
    Assertions.assertTrue(entities.isEmpty());

    verify(orgUnitRepository, times(0)).findAll();
  }

  @Test
  @DisplayName("Find all org units by code happy path should succeed")
  void findAllOrgUnitsByCode_happyPath_shouldSucceed() {
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
    orgUnit.setExpiryDate(LocalDate.now().minusYears(1L));
    orgUnit.setUpdateTimestamp(LocalDate.now());

    when(orgUnitRepository.findAllByOrgUnitCodeIn(List.of("DAS"))).thenReturn(List.of(orgUnit));
    List<OrgUnitEntity> entities = orgUnitService.findAllOrgUnitsByCode(List.of("DAS"));

    Assertions.assertNotNull(entities);
    Assertions.assertEquals(1, entities.size());

    OrgUnitEntity responseOrg = entities.get(0);
    Assertions.assertTrue(responseOrg.getExpiryDate().isBefore(LocalDate.now()));
    Assertions.assertEquals(22L, responseOrg.getOrgUnitNo());
    Assertions.assertEquals("DAS", responseOrg.getOrgUnitCode());
    Assertions.assertEquals("DAS Name", responseOrg.getOrgUnitName());
    Assertions.assertEquals("123", responseOrg.getLocationCode());
    Assertions.assertEquals("1", responseOrg.getAreaCode());
    Assertions.assertEquals("25436521", responseOrg.getTelephoneNo());
    Assertions.assertEquals('R', responseOrg.getOrgLevelCode());
    Assertions.assertEquals("RR", responseOrg.getOfficeNameCode());
    Assertions.assertEquals(12L, responseOrg.getRollupRegionNo());
    Assertions.assertEquals("19", responseOrg.getRollupRegionCode());
    Assertions.assertEquals(13L, responseOrg.getRollupDistNo());
    Assertions.assertEquals("25", responseOrg.getRollupDistCode());
  }

  @Test
  @DisplayName("Find all org units by code not found should succeed")
  void findAllOrgUnitsByCode_notFound_shouldSucceed() {
    when(orgUnitRepository.findAllByOrgUnitCodeIn(List.of("DAS"))).thenReturn(List.of());
    List<OrgUnitEntity> entities = orgUnitService.findAllOrgUnitsByCode(List.of("DAS"));

    Assertions.assertNotNull(entities);
    Assertions.assertTrue(entities.isEmpty());
  }
}
