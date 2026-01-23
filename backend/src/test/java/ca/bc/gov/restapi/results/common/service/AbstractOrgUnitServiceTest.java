package ca.bc.gov.restapi.results.common.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.projection.OrgUnitProjection;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Org Unit Service | Contract")
public abstract class AbstractOrgUnitServiceTest<T extends OrgUnitService> {

  @Mock
  protected OrgUnitRepository orgUnitRepository;

  protected T orgUnitService;

  private final SilvaConfiguration
      silvaConfiguration = new SilvaConfiguration().withOrgUnits(List.of("DAS"));

  protected abstract T createService(OrgUnitRepository orgUnitRepository, SilvaConfiguration silvaConfiguration);

  @BeforeEach
  void setup() {
    orgUnitService = createService(orgUnitRepository, silvaConfiguration);
  }

  @Test
  @DisplayName("Find all org units happy path should succeed")
  void findAllOrgUnits_happyPath_shouldSucceed() {
    OrgUnitProjection orgUnit = mock(OrgUnitProjection.class);
    when(orgUnit.getOrgUnitCode()).thenReturn("DAS");
    when(orgUnit.getOrgUnitName()).thenReturn("DAS Name");

    when(orgUnitRepository.findAllByOrgUnitCodeIn(List.of("DAS")))
        .thenReturn(List.of(orgUnit));
    List<CodeDescriptionDto> entities = orgUnitService.findAllOrgUnits();

    Assertions.assertNotNull(entities);
    Assertions.assertEquals(1, entities.size());

    CodeDescriptionDto responseOrg = entities.get(0);
    Assertions.assertEquals("DAS", responseOrg.code());
    Assertions.assertEquals("DAS Name", responseOrg.description());
  }

  @Test
  @DisplayName("Find all org units empty response should succeed")
  void findAllOrgUnits_emptyResponse_shouldSucceed() {

    when(orgUnitRepository.findAllByOrgUnitCodeIn(List.of("DAS"))).thenReturn(List.of());
    List<CodeDescriptionDto> entities = orgUnitService.findAllOrgUnits();

    Assertions.assertNotNull(entities);
    Assertions.assertTrue(entities.isEmpty());
  }

  @Test
  @DisplayName("Find all org units empty response should succeed")
  void findNoOrgUnits_emptyResponse_shouldSucceed() {

    when(orgUnitRepository.findAllByOrgUnitCodeIn(List.of("DAS"))).thenReturn(List.of());
    List<CodeDescriptionDto> entities = orgUnitService.findAllOrgUnits();

    Assertions.assertNotNull(entities);
    Assertions.assertTrue(entities.isEmpty());
  }
}
