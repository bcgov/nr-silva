package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.postgres.dto.BecDataDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingLayerDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesDto;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeClientEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeLayerEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeLayerSpeciesEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeOrgUnitEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeSiteSeriesEntity;
import ca.bc.gov.restapi.results.postgres.enums.StockingLayerType;
import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesMilestone;
import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesType;
import ca.bc.gov.restapi.results.postgres.enums.StockingStandardAuthorityType;
import ca.bc.gov.restapi.results.postgres.enums.StockingType;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeClientPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeLayerPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeLayerSpeciesPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeOrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeSiteSeriesPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StockingStandardsPostgresRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | CreateStockingStandardService")
class CreateStockingStandardServiceTest {

  @Mock private StockingStandardValidationService validationService;
  @Mock private StockingStandardsPostgresRepository standardsRegimeRepository;
  @Mock private StandardsRegimeOrgUnitPostgresRepository orgUnitLinkRepository;
  @Mock private StandardsRegimeClientPostgresRepository clientLinkRepository;
  @Mock private StandardsRegimeSiteSeriesPostgresRepository siteSeriesRepository;
  @Mock private StandardsRegimeLayerPostgresRepository layerRepository;
  @Mock private StandardsRegimeLayerSpeciesPostgresRepository layerSpeciesRepository;
  @Mock private LoggedUserHelper loggedUserHelper;
  @Mock private JdbcTemplate jdbcTemplate;

  private CreateStockingStandardService service;

  @BeforeEach
  void setUp() {
    service = new CreateStockingStandardService(validationService, standardsRegimeRepository,
        orgUnitLinkRepository, clientLinkRepository, siteSeriesRepository, layerRepository,
        layerSpeciesRepository, loggedUserHelper, jdbcTemplate);
  }

  @Test
  @DisplayName("Operational Plan persists mapped links, BEC rows, layers and milestone species")
  void create_operationalPlan_persistsCompleteGraph() {
    CreateStockingStandardRequestDto request = operationalPlanRequest();
    when(validationService.validate(request)).thenReturn(List.of(11L, 12L));
    when(loggedUserHelper.getAuditUserId()).thenReturn("IDIR\\tester");
    when(jdbcTemplate.queryForObject(anyString(), eq(Long.class)))
        .thenReturn(100L, 200L, 301L, 302L, 303L, 304L);

    CreateStockingStandardResponseDto response = service.create(request);

    assertThat(response.stockingStandardsId()).isEqualTo(100L);
    ArgumentCaptor<StandardsRegimeEntity> standard =
        ArgumentCaptor.forClass(StandardsRegimeEntity.class);
    verify(standardsRegimeRepository).save(standard.capture());
    assertThat(standard.getValue())
        .extracting(
            StandardsRegimeEntity::getId,
            StandardsRegimeEntity::getStandardsRegimeName,
            StandardsRegimeEntity::getStandardsRegimeStatusCode,
            StandardsRegimeEntity::getMofDefaultStandardInd,
            StandardsRegimeEntity::getAlternativeMethodInd,
            StandardsRegimeEntity::getRegenObligationInd)
        .containsExactly(100L, "Standard name", "DFT", "N", "N", "Y");

    ArgumentCaptor<StandardsRegimeOrgUnitEntity> orgUnits =
        ArgumentCaptor.forClass(StandardsRegimeOrgUnitEntity.class);
    verify(orgUnitLinkRepository, times(2)).save(orgUnits.capture());
    assertThat(orgUnits.getAllValues()).extracting(StandardsRegimeOrgUnitEntity::getOrgUnitNo)
        .containsExactly(11L, 12L);
    ArgumentCaptor<StandardsRegimeClientEntity> clients =
        ArgumentCaptor.forClass(StandardsRegimeClientEntity.class);
    verify(clientLinkRepository).save(clients.capture());
    assertThat(clients.getValue())
        .extracting(
            StandardsRegimeClientEntity::getStandardsRegimeId,
            StandardsRegimeClientEntity::getClientNumber)
        .containsExactly(100L, "00012797");
    ArgumentCaptor<StandardsRegimeSiteSeriesEntity> bec =
        ArgumentCaptor.forClass(StandardsRegimeSiteSeriesEntity.class);
    verify(siteSeriesRepository).save(bec.capture());
    assertThat(bec.getValue())
        .extracting(
            StandardsRegimeSiteSeriesEntity::getId,
            StandardsRegimeSiteSeriesEntity::getStandardsRegimeId,
            StandardsRegimeSiteSeriesEntity::getBgcVariant)
        .containsExactly(200L, 100L, "1");

    ArgumentCaptor<StandardsRegimeLayerEntity> layers =
        ArgumentCaptor.forClass(StandardsRegimeLayerEntity.class);
    verify(layerRepository, times(4)).save(layers.capture());
    assertThat(layers.getAllValues())
        .extracting(
            StandardsRegimeLayerEntity::getId, StandardsRegimeLayerEntity::getStockingLayerCode)
        .containsExactly(
            org.assertj.core.groups.Tuple.tuple(301L, "4"),
            org.assertj.core.groups.Tuple.tuple(302L, "3"),
            org.assertj.core.groups.Tuple.tuple(303L, "2"),
            org.assertj.core.groups.Tuple.tuple(304L, "1"));
    ArgumentCaptor<StandardsRegimeLayerSpeciesEntity> species =
        ArgumentCaptor.forClass(StandardsRegimeLayerSpeciesEntity.class);
    verify(layerSpeciesRepository, times(12)).save(species.capture());
    assertThat(species.getAllValues())
        .allSatisfy(
            value -> assertThat(value.getStandardsRegimeLayerId()).isIn(301L, 302L, 303L, 304L));
    assertThat(species.getAllValues())
        .filteredOn(value -> value.getSilvTreeSpeciesCode().equals("CW"))
        .allSatisfy(
            value ->
                assertThat(value)
                    .extracting(
                        StandardsRegimeLayerSpeciesEntity::getRegenMilestoneInd,
                        StandardsRegimeLayerSpeciesEntity::getFreeGrowingMilestoneInd)
                    .containsExactly("Y", "Y"));
  }

  @Test
  @DisplayName("Ministry Default maps its flags and does not persist client links")
  void create_ministryDefault_mapsAuthorityFlagsWithoutClients() {
    CreateStockingStandardRequestDto request = ministryDefaultRequest();
    when(validationService.validate(request)).thenReturn(List.of(90L));
    when(loggedUserHelper.getAuditUserId()).thenReturn("IDIR\\tester");
    when(jdbcTemplate.queryForObject(anyString(), eq(Long.class))).thenReturn(101L, 401L);

    service.create(request);

    ArgumentCaptor<StandardsRegimeEntity> standard =
        ArgumentCaptor.forClass(StandardsRegimeEntity.class);
    verify(standardsRegimeRepository).save(standard.capture());
    assertThat(standard.getValue())
        .extracting(
            StandardsRegimeEntity::getMofDefaultStandardInd,
            StandardsRegimeEntity::getAlternativeMethodInd,
            StandardsRegimeEntity::getRegenObligationInd)
        .containsExactly("Y", "Y", "N");
    verify(clientLinkRepository, org.mockito.Mockito.never()).save(any());
  }

  @Test
  @DisplayName("No layer-species links are created when species are omitted")
  void create_withoutSpecies_doesNotPersistLayerSpecies() {
    CreateStockingStandardRequestDto base = operationalPlanRequest();
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            base.objective(), base.name(), base.location(), base.authorityType(), base.orgUnitCodes(),
            base.clientNumbers(), base.becInfoSelected(), base.alternativeMethodSelected(), base.becData(),
            null, base.stockingType(), base.regenDelayYears(), base.freeGrowingYears(), base.earlyYears(),
            base.lateYears(), base.layerType(), base.singleLayer(), base.multiLayers(),
            base.additionalStandards());
    when(validationService.validate(request)).thenReturn(List.of(11L, 12L));
    when(loggedUserHelper.getAuditUserId()).thenReturn("IDIR\\tester");
    when(jdbcTemplate.queryForObject(anyString(), eq(Long.class)))
        .thenReturn(100L, 200L, 301L, 302L, 303L, 304L);

    service.create(request);

    verify(layerSpeciesRepository, org.mockito.Mockito.never()).save(any());
  }

  private CreateStockingStandardRequestDto operationalPlanRequest() {
    List<StockingLayerDto> layers =
        List.of(
            new StockingLayerDto(
                "4", 1000, 800, BigDecimal.ONE, 1200, null, null, null, null, 15, "CM"),
            new StockingLayerDto(
                "3", 1000, 800, BigDecimal.ONE, 1200, null, null, null, null, 16, "%"),
            new StockingLayerDto(
                "2", 1000, 800, BigDecimal.ONE, 1200, 10, 100, 200, 300, null, null),
            new StockingLayerDto(
                "1", 1000, 800, BigDecimal.ONE, 1200, 10, 100, 200, 300, null, null));
    return new CreateStockingStandardRequestDto(
        "Objective",
        " Standard name ",
        " Location ",
        StockingStandardAuthorityType.OPERATIONAL_PLAN,
        List.of("DAS", "DCC"),
        List.of(" 00012797 "),
        true,
        false,
        List.of(new BecDataDto("CWH", "wh", "1", null, "01", null)),
        List.of(
            new StockingSpeciesDto(
                "CW", StockingSpeciesType.PREFERRED, BigDecimal.ONE, StockingSpeciesMilestone.BOTH),
            new StockingSpeciesDto(
                "HW", StockingSpeciesType.ACCEPTABLE, null, StockingSpeciesMilestone.REGEN),
            new StockingSpeciesDto(
                "BA",
                StockingSpeciesType.ECOLOGICALLY_SUITABLE,
                null,
                StockingSpeciesMilestone.FREE_GROWING)),
        StockingType.REGEN_OBLIGATION,
        1,
        20,
        null,
        null,
        StockingLayerType.MULTI,
        null,
        layers,
        null);
  }

  private CreateStockingStandardRequestDto ministryDefaultRequest() {
    return new CreateStockingStandardRequestDto(
        "Objective",
        null,
        null,
        StockingStandardAuthorityType.MINISTRY_DEFAULT,
        null,
        null,
        false,
        true,
        null,
        List.of(
            new StockingSpeciesDto(
                "CW", StockingSpeciesType.PREFERRED, null, StockingSpeciesMilestone.BOTH)),
        StockingType.STOCKING_REQUIREMENT,
        null,
        null,
        1,
        2,
        StockingLayerType.SINGLE,
        new StockingLayerDto("I", null, null, null, null, null, null, null, null, null, null),
        null,
        null);
  }
}
