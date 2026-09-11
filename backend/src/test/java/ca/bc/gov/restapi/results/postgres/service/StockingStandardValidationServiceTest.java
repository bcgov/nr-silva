package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.enums.Role;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.postgres.dto.BecDataDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingLayerDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesDto;
import ca.bc.gov.restapi.results.postgres.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.postgres.entity.SiteSeriesCatalogueEntity;
import ca.bc.gov.restapi.results.postgres.enums.StockingLayerType;
import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesMilestone;
import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesType;
import ca.bc.gov.restapi.results.postgres.enums.StockingStandardAuthorityType;
import ca.bc.gov.restapi.results.postgres.enums.StockingType;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvTreeSpeciesCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SiteSeriesCataloguePostgresRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Unit Test | StockingStandardValidationService")
class StockingStandardValidationServiceTest {

  @Mock private OrgUnitPostgresRepository orgUnitRepository;
  @Mock private SilvTreeSpeciesCodePostgresRepository speciesCodeRepository;
  @Mock private SiteSeriesCataloguePostgresRepository siteSeriesCatalogueRepository;
  @Mock private LoggedUserHelper loggedUserHelper;

  private StockingStandardValidationService service;

  private static final StockingSpeciesDto VALID_SPECIES =
      new StockingSpeciesDto("CW", StockingSpeciesType.PREFERRED, null, StockingSpeciesMilestone.BOTH);
  private static final StockingLayerDto VALID_SINGLE_LAYER =
      new StockingLayerDto("I", null, null, null, null, null, null, null, null, null, null);

  private void assertRejected(CreateStockingStandardRequestDto request, HttpStatus expectedStatus) {
    assertThatThrownBy(() -> service.validate(request))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(expectedStatus));
  }

  @BeforeEach
  void setUp() {
    service =
        new StockingStandardValidationService(
            orgUnitRepository, speciesCodeRepository, siteSeriesCatalogueRepository, loggedUserHelper);
    lenient().when(speciesCodeRepository.existsById(anyString())).thenReturn(true);
    lenient()
        .when(
            siteSeriesCatalogueRepository.findMatchingBecCombo(
                anyString(), anyString(), any(), any(), anyString(), any()))
        .thenReturn(List.of(SiteSeriesCatalogueEntity.builder().id(1L).build()));
    allow(Role.SUBMITTER, "00012797");
  }

  private CreateStockingStandardRequestDto operationalPlanRequest(
      List<String> orgUnitCodes,
      List<String> clientNumbers,
      boolean becInfoSelected,
      boolean altMethodSelected,
      List<BecDataDto> becData) {
    return new CreateStockingStandardRequestDto(
        "Objective",
        "Name",
        "Location",
        StockingStandardAuthorityType.OPERATIONAL_PLAN,
        orgUnitCodes,
        clientNumbers == null ? null : clientNumbers.isEmpty() ? List.of("00012797") : clientNumbers,
        becInfoSelected,
        altMethodSelected,
        becData,
        List.of(VALID_SPECIES),
        StockingType.REGEN_OBLIGATION,
        1,
        20,
        null,
        null,
        StockingLayerType.SINGLE,
        VALID_SINGLE_LAYER,
        null,
        null);
  }

  @Test
  @DisplayName("Ministry Default resolves the HFP org unit for an authorized user")
  void ministryDefault_resolvesHfpOrgUnit() {
    when(orgUnitRepository.findByOrgUnitCode("HFP"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(90L).orgUnitCode("HFP").build()));

    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective",
            null,
            null,
            StockingStandardAuthorityType.MINISTRY_DEFAULT,
            null,
            null,
            false,
            true,
            null,
            List.of(VALID_SPECIES),
            StockingType.REGEN_OBLIGATION,
            1,
            20,
            null,
            null,
            StockingLayerType.SINGLE,
            VALID_SINGLE_LAYER,
            null,
            null);

    List<Long> orgUnitNos = service.validate(request);

    assertThat(orgUnitNos).containsExactly(90L);
  }

  @Test
  @DisplayName("Ministry Default without the configured HFP org unit fails as a server error")
  void ministryDefault_withoutHfpOrgUnit_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("HFP")).thenReturn(Optional.empty());

    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective", null, null, StockingStandardAuthorityType.MINISTRY_DEFAULT, null, null,
            false, true, null, List.of(VALID_SPECIES), StockingType.REGEN_OBLIGATION, 1, 20,
            null, null, StockingLayerType.SINGLE, VALID_SINGLE_LAYER, null, null);

    assertRejected(request, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  @DisplayName("Operational Plan with no org units is rejected")
  void operationalPlan_noOrgUnits_isRejected() {
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(List.of(), List.of(), false, true, null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Operational Plan with an unknown org unit code is rejected")
  void operationalPlan_unknownOrgUnitCode_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("ZZZ")).thenReturn(Optional.empty());
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(List.of("ZZZ"), List.of(), false, true, null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Client without an allowed role for the client number is forbidden")
  void client_withoutAllowedRole_isForbidden() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    denyAll("00012797");
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(List.of("DAS"), List.of("00012797"), false, true, null);

    assertRejected(request, HttpStatus.FORBIDDEN);
  }

  @Test
  @DisplayName("Client with the ADMIN role for the client number passes")
  void client_withAdminRole_passes() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    allow(Role.ADMIN, "00012797");
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(List.of("DAS"), List.of("00012797"), false, true, null);

    assertThat(service.validate(request)).containsExactly(1L);
  }

  @Test
  @DisplayName("Client with the APPROVER role for the client number passes")
  void client_withApproverRole_passes() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    allow(Role.APPROVER, "00012797");

    assertThat(service.validate(operationalPlanRequest(
        List.of("DAS"), List.of("00012797"), false, true, null))).containsExactly(1L);
  }

  @Test
  @DisplayName("Operational Plan without a client is rejected")
  void operationalPlan_withoutClient_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));

    assertRejected(operationalPlanRequest(List.of("DAS"), null, false, true, null), HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Ministry Default with a supplied client is rejected")
  void ministryDefault_withClient_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("HFP"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(90L).orgUnitCode("HFP").build()));
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective", null, null, StockingStandardAuthorityType.MINISTRY_DEFAULT, null,
            List.of("00012797"), false, true, null, List.of(VALID_SPECIES),
            StockingType.REGEN_OBLIGATION, 1, 20, null, null, StockingLayerType.SINGLE,
            VALID_SINGLE_LAYER, null, null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Operational Plan rejects an unauthorized associated client")
  void operationalPlan_unauthorizedAssociatedClient_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));

    assertRejected(operationalPlanRequest(
        List.of("DAS"), List.of("00012797", "00000001"), false, true, null), HttpStatus.FORBIDDEN);
  }

  @Test
  @DisplayName("Duplicate normalized client numbers are rejected")
  void duplicateClientNumbers_areRejected() {
    assertRejected(
        operationalPlanRequest(
            List.of("DAS"), List.of("00012797", " 00012797 "), false, true, null),
        HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Duplicate org unit codes are rejected")
  void duplicateOrgUnitCodes_areRejected() {
    assertRejected(
        operationalPlanRequest(List.of("DAS", " DAS "), List.of("00012797"), false, true, null),
        HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Duplicate species codes are rejected")
  void duplicateSpeciesCodes_areRejected() {
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective", "Name", "Location", StockingStandardAuthorityType.OPERATIONAL_PLAN,
            List.of("DAS"), List.of("00012797"), false, true, null,
            List.of(VALID_SPECIES, VALID_SPECIES), StockingType.REGEN_OBLIGATION, 1, 20, null,
            null, StockingLayerType.SINGLE, VALID_SINGLE_LAYER, null, null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("BEC site phase is included in catalogue validation")
  void becSitePhase_isValidated() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    BecDataDto bec = new BecDataDto("CWH", "wh", "1", null, "01", "A");

    service.validate(operationalPlanRequest(List.of("DAS"), List.of("00012797"), true, false, List.of(bec)));

    verify(siteSeriesCatalogueRepository)
        .findMatchingBecCombo("CWH", "wh", "1", null, "01", "A");
  }

  private void denyAll(String clientNumber) {
    for (Role role : List.of(Role.SUBMITTER, Role.APPROVER, Role.ADMIN)) {
      lenient().doReturn(false).when(loggedUserHelper).hasAbstractRole(role, clientNumber);
    }
  }

  private void allow(Role role, String clientNumber) {
    denyAll(clientNumber);
    doReturn(true).when(loggedUserHelper).hasAbstractRole(role, clientNumber);
  }

  @Test
  @DisplayName("Neither BEC info nor alternative method selected is rejected")
  void neitherBecNorAltMethod_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(List.of("DAS"), List.of("00012797"), false, false, null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("BEC info selected with no BEC entries is rejected")
  void becInfoSelected_noBecData_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(List.of("DAS"), List.of("00012797"), true, false, List.of());

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("BEC entries supplied when BEC info is not selected is rejected")
  void becData_withoutBecInfoSelected_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(
            List.of("DAS"),
            List.of("00012797"),
            false,
            true,
            List.of(new BecDataDto("CWH", "wh1", null, null, "01", null)));

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Unknown BEC combination is rejected")
  void unknownBecCombo_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    when(siteSeriesCatalogueRepository.findMatchingBecCombo(
            "ZZZ", "zz", null, null, "99", null))
        .thenReturn(List.of());
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(
            List.of("DAS"),
            List.of("00012797"),
            true,
            false,
            List.of(new BecDataDto("ZZZ", "zz", null, null, "99", null)));

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Unknown species code is rejected")
  void unknownSpeciesCode_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    when(speciesCodeRepository.existsById("ZZ")).thenReturn(false);
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective",
            "Name",
            "Location",
            StockingStandardAuthorityType.OPERATIONAL_PLAN,
            List.of("DAS"),
            List.of("00012797"),
            false,
            true,
            null,
            List.of(new StockingSpeciesDto("ZZ", StockingSpeciesType.PREFERRED, null, StockingSpeciesMilestone.BOTH)),
            StockingType.REGEN_OBLIGATION,
            1,
            20,
            null,
            null,
            StockingLayerType.SINGLE,
            VALID_SINGLE_LAYER,
            null,
            null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Regen Obligation missing regen delay/free growing years is rejected")
  void regenObligation_missingFields_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective",
            "Name",
            "Location",
            StockingStandardAuthorityType.OPERATIONAL_PLAN,
            List.of("DAS"),
            List.of("00012797"),
            false,
            true,
            null,
            List.of(VALID_SPECIES),
            StockingType.REGEN_OBLIGATION,
            null,
            null,
            null,
            null,
            StockingLayerType.SINGLE,
            VALID_SINGLE_LAYER,
            null,
            null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Stocking Requirement with earlyYears greater than lateYears is rejected")
  void stockingRequirement_earlyGreaterThanLate_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective",
            "Name",
            "Location",
            StockingStandardAuthorityType.OPERATIONAL_PLAN,
            List.of("DAS"),
            List.of("00012797"),
            false,
            true,
            null,
            List.of(VALID_SPECIES),
            StockingType.STOCKING_REQUIREMENT,
            null,
            null,
            10,
            5,
            StockingLayerType.SINGLE,
            VALID_SINGLE_LAYER,
            null,
            null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("SINGLE layer type with multiLayers supplied is rejected")
  void singleLayerType_withMultiLayers_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective",
            "Name",
            "Location",
            StockingStandardAuthorityType.OPERATIONAL_PLAN,
            List.of("DAS"),
            List.of("00012797"),
            false,
            true,
            null,
            List.of(VALID_SPECIES),
            StockingType.REGEN_OBLIGATION,
            1,
            20,
            null,
            null,
            StockingLayerType.SINGLE,
            VALID_SINGLE_LAYER,
            List.of(VALID_SINGLE_LAYER),
            null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("MULTI layer type with the wrong number of layers is rejected")
  void multiLayerType_wrongLayerCount_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective",
            "Name",
            "Location",
            StockingStandardAuthorityType.OPERATIONAL_PLAN,
            List.of("DAS"),
            List.of("00012797"),
            false,
            true,
            null,
            List.of(VALID_SPECIES),
            StockingType.REGEN_OBLIGATION,
            1,
            20,
            null,
            null,
            StockingLayerType.MULTI,
            null,
            List.of(
                new StockingLayerDto("4", null, null, null, null, null, null, null, null, null, null),
                new StockingLayerDto("3", null, null, null, null, null, null, null, null, null, null)),
            null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("MULTI layer type with a single layer is rejected")
  void multiLayerType_withSingleLayer_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(List.of("DAS"), List.of("00012797"), false, true, null);
    request =
        new CreateStockingStandardRequestDto(
            request.objective(), request.name(), request.location(), request.authorityType(),
            request.orgUnitCodes(), request.clientNumbers(), request.becInfoSelected(),
            request.alternativeMethodSelected(), request.becData(), request.species(),
            request.stockingType(), request.regenDelayYears(), request.freeGrowingYears(),
            request.earlyYears(), request.lateYears(), StockingLayerType.MULTI, VALID_SINGLE_LAYER,
            List.of(VALID_SINGLE_LAYER, VALID_SINGLE_LAYER, VALID_SINGLE_LAYER, VALID_SINGLE_LAYER), request.additionalStandards());

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("A field not applicable to the layer number is rejected")
  void disallowedFieldForLayer_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    // minResidualBasalArea is only applicable to layers 1/2, not layer 3.
    StockingLayerDto layer3WithResidualBasalArea =
        new StockingLayerDto("3", null, null, null, null, 10, null, null, null, null, null);
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective",
            "Name",
            "Location",
            StockingStandardAuthorityType.OPERATIONAL_PLAN,
            List.of("DAS"),
            List.of("00012797"),
            false,
            true,
            null,
            List.of(VALID_SPECIES),
            StockingType.REGEN_OBLIGATION,
            1,
            20,
            null,
            null,
            StockingLayerType.MULTI,
            null,
            List.of(
                layer3WithResidualBasalArea,
                new StockingLayerDto("4", null, null, null, null, null, null, null, null, null, null),
                new StockingLayerDto("2", null, null, null, null, null, null, null, null, null, null),
                new StockingLayerDto("1", null, null, null, null, null, null, null, null, null, null)),
            null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("heightRelativeToComp without a unit code is rejected")
  void heightRelativeToComp_withoutUnitCode_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    StockingLayerDto layer =
        new StockingLayerDto("I", null, null, null, null, null, null, null, null, 15, null);
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective",
            "Name",
            "Location",
            StockingStandardAuthorityType.OPERATIONAL_PLAN,
            List.of("DAS"),
            List.of("00012797"),
            false,
            true,
            null,
            List.of(VALID_SPECIES),
            StockingType.REGEN_OBLIGATION,
            1,
            20,
            null,
            null,
            StockingLayerType.SINGLE,
            layer,
            null,
            null);

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("heightRelativeToComp with a valid CM unit code passes")
  void heightRelativeToComp_withValidUnitCode_passes() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    StockingLayerDto layer =
        new StockingLayerDto("I", null, null, null, null, null, null, null, null, 15, "CM");
    CreateStockingStandardRequestDto request =
        new CreateStockingStandardRequestDto(
            "Objective",
            "Name",
            "Location",
            StockingStandardAuthorityType.OPERATIONAL_PLAN,
            List.of("DAS"),
            List.of("00012797"),
            false,
            true,
            null,
            List.of(VALID_SPECIES),
            StockingType.REGEN_OBLIGATION,
            1,
            20,
            null,
            null,
            StockingLayerType.SINGLE,
            layer,
            null,
            null);

    assertThat(service.validate(request)).containsExactly(1L);
  }

  @Test
  @DisplayName("heightRelativeToComp with an unsupported unit code is rejected")
  void heightRelativeToComp_withUnsupportedUnitCode_isRejected() {
    when(orgUnitRepository.findByOrgUnitCode("DAS"))
        .thenReturn(Optional.of(OrgUnitEntity.builder().orgUnitNo(1L).orgUnitCode("DAS").build()));
    StockingLayerDto layer =
        new StockingLayerDto("I", null, null, null, null, null, null, null, null, 15, "M");
    CreateStockingStandardRequestDto request =
        operationalPlanRequest(List.of("DAS"), List.of("00012797"), false, true, null);
    request =
        new CreateStockingStandardRequestDto(
            request.objective(), request.name(), request.location(), request.authorityType(),
            request.orgUnitCodes(), request.clientNumbers(), request.becInfoSelected(),
            request.alternativeMethodSelected(), request.becData(), request.species(),
            request.stockingType(), request.regenDelayYears(), request.freeGrowingYears(),
            request.earlyYears(), request.lateYears(), request.layerType(), layer,
            request.multiLayers(), request.additionalStandards());

    assertRejected(request, HttpStatus.BAD_REQUEST);
  }
}
