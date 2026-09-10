package ca.bc.gov.restapi.results.postgres.service;

import static ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants.MINISTRY_OF_FORESTS_CLIENT_NUMBER;

import ca.bc.gov.restapi.results.common.enums.Role;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.postgres.dto.BecDataDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingLayerDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesDto;
import ca.bc.gov.restapi.results.postgres.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.postgres.enums.StockingLayerType;
import ca.bc.gov.restapi.results.postgres.enums.StockingStandardAuthorityType;
import ca.bc.gov.restapi.results.postgres.enums.StockingType;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvTreeSpeciesCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SiteSeriesCataloguePostgresRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Validates a {@link CreateStockingStandardRequestDto} before any database write occurs. All
 * failures are hard rejections (no partial/soft warnings) surfaced as {@link
 * ResponseStatusException}.
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class StockingStandardValidationService {

  private static final String MINISTRY_DEFAULT_ORG_UNIT_CODE = "HFP";
  private static final Set<String> VALID_HEIGHT_RELATIVE_UNIT_CODES = Set.of("CM", "%");
  private static final Set<String> MULTI_LAYER_CODES = Set.of("4", "3", "2", "1");
  private static final Set<String> LAYER_1_2_ONLY_CODES = Set.of("2", "1");
  private static final Set<String> LAYER_3_4_ONLY_CODES = Set.of("4", "3");
  private static final Set<Role> CREATE_STOCKING_STANDARD_ROLES =
      Set.of(Role.SUBMITTER, Role.APPROVER, Role.ADMIN);

  private final OrgUnitPostgresRepository orgUnitRepository;
  private final SilvTreeSpeciesCodePostgresRepository speciesCodeRepository;
  private final SiteSeriesCataloguePostgresRepository siteSeriesCatalogueRepository;
  private final LoggedUserHelper loggedUserHelper;

  /**
   * Validates the request and resolves the org unit numbers to use for authority.
   *
   * @param dto the incoming request
   * @return the resolved org unit numbers to persist against standards_regime_org_unit
   * @throws ResponseStatusException with 400/403 on any validation failure
   */
  public List<Long> validate(CreateStockingStandardRequestDto dto) {
    List<Long> orgUnitNos = validateAuthority(dto);
    validateClients(dto);
    validateBec(dto);
    validateSpecies(dto);
    validateStockingType(dto);
    validateLayers(dto);
    return orgUnitNos;
  }

  private List<Long> validateAuthority(CreateStockingStandardRequestDto dto) {
    if (dto.authorityType() == StockingStandardAuthorityType.MINISTRY_DEFAULT) {
      // Ministry Default forces the HFP org unit server-side; any client-supplied org units are
      // ignored (this is a Silva-only rule, not inherited from legacy RESULTS).
      OrgUnitEntity hfp =
          orgUnitRepository
              .findByOrgUnitCode(MINISTRY_DEFAULT_ORG_UNIT_CODE)
              .orElseThrow(
                  () ->
                      new ResponseStatusException(
                          HttpStatus.INTERNAL_SERVER_ERROR,
                          "Ministry Default org unit '" + MINISTRY_DEFAULT_ORG_UNIT_CODE
                              + "' is not configured"));
      return List.of(hfp.getOrgUnitNo());
    }

    // OPERATIONAL_PLAN: at least one org unit is required, and every code must exist.
    List<String> orgUnitCodes = dto.orgUnitCodes();
    if (orgUnitCodes == null || orgUnitCodes.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "At least one org unit is required for Operational Plan authority");
    }
    List<Long> orgUnitNos = new ArrayList<>();
    List<String> notFound = new ArrayList<>();
    for (String code : orgUnitCodes) {
      orgUnitRepository
          .findByOrgUnitCode(code.trim())
          .ifPresentOrElse(
              ou -> orgUnitNos.add(ou.getOrgUnitNo()), () -> notFound.add(code));
    }
    if (!notFound.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Unknown org unit code(s): " + String.join(", ", notFound));
    }
    return orgUnitNos;
  }

  private void validateClients(CreateStockingStandardRequestDto dto) {
    List<String> clientNumbers = dto.clientNumbers();
    if (dto.authorityType() == StockingStandardAuthorityType.MINISTRY_DEFAULT) {
      if (clientNumbers != null && !clientNumbers.isEmpty()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "clientNumbers must not be supplied for Ministry Default authority");
      }
      validateCreateRole(MINISTRY_OF_FORESTS_CLIENT_NUMBER);
      return;
    }
    if (clientNumbers == null || clientNumbers.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "At least one client is required for Operational Plan authority");
    }
    for (String clientNumber : clientNumbers) {
      validateCreateRole(clientNumber.trim());
    }
  }

  private void validateCreateRole(String clientNumber) {
    boolean authorized =
        CREATE_STOCKING_STANDARD_ROLES.stream()
            .anyMatch(role -> loggedUserHelper.hasAbstractRole(role, clientNumber));
    if (!authorized) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Not authorised to create a stocking standard for " + clientNumber);
    }
  }

  private void validateBec(CreateStockingStandardRequestDto dto) {
    boolean becInfo = Boolean.TRUE.equals(dto.becInfoSelected());
    boolean altMethod = Boolean.TRUE.equals(dto.alternativeMethodSelected());
    if (!becInfo && !altMethod) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Select at least one of BEC information or Alternative method");
    }

    List<BecDataDto> becData = dto.becData();
    if (becInfo) {
      if (becData == null || becData.isEmpty()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "At least one BEC entry is required when BEC information is selected");
      }
      List<String> invalidCombos = new ArrayList<>();
      for (BecDataDto bec : becData) {
        boolean exists =
            !siteSeriesCatalogueRepository
                .findMatchingBecCombo(
                    bec.bgcZoneCode(),
                    bec.bgcSubzoneCode(),
                    bec.variant(),
                    bec.phase(),
                    bec.siteSeries())
                .isEmpty();
        if (!exists) {
          invalidCombos.add(
              bec.bgcZoneCode()
                  + "/"
                  + bec.bgcSubzoneCode()
                  + "/"
                  + StringUtils.defaultString(bec.variant())
                  + "/"
                  + StringUtils.defaultString(bec.phase())
                  + "/"
                  + bec.siteSeries());
        }
      }
      if (!invalidCombos.isEmpty()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Unknown BEC combination(s): " + String.join(", ", invalidCombos));
      }
    } else if (becData != null && !becData.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "BEC entries must not be supplied when BEC information is not selected");
    }
  }

  private void validateSpecies(CreateStockingStandardRequestDto dto) {
    if (dto.species() == null) {
      return;
    }
    List<String> notFound = new ArrayList<>();
    for (StockingSpeciesDto species : dto.species()) {
      if (!speciesCodeRepository.existsById(species.speciesCode().trim())) {
        notFound.add(species.speciesCode());
      }
    }
    if (!notFound.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Unknown species code(s): " + String.join(", ", notFound));
    }
  }

  private void validateStockingType(CreateStockingStandardRequestDto dto) {
    if (dto.stockingType() == StockingType.REGEN_OBLIGATION) {
      if (dto.regenDelayYears() == null || dto.freeGrowingYears() == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "regenDelayYears and freeGrowingYears are required for a Regen Obligation standard");
      }
    } else {
      if (dto.earlyYears() == null || dto.lateYears() == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "earlyYears and lateYears are required for a Stocking Requirement standard");
      }
      if (dto.earlyYears() > dto.lateYears()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "earlyYears must be less than or equal to lateYears");
      }
    }
  }

  private void validateLayers(CreateStockingStandardRequestDto dto) {
    if (dto.layerType() == StockingLayerType.SINGLE) {
      if (dto.singleLayer() == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "singleLayer is required when layerType is SINGLE");
      }
      if (dto.multiLayers() != null && !dto.multiLayers().isEmpty()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "multiLayers must not be supplied when layerType is SINGLE");
      }
      validateLayerFields(dto.singleLayer(), false);
    } else {
      if (dto.singleLayer() != null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "singleLayer must not be supplied when layerType is MULTI");
      }
      List<StockingLayerDto> multiLayers = dto.multiLayers();
      if (multiLayers == null || multiLayers.size() != 4) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Exactly 4 layer entries (codes 4, 3, 2, 1) are required when layerType is MULTI");
      }
      Set<String> suppliedCodes =
          multiLayers.stream().map(StockingLayerDto::layerCode).collect(java.util.stream.Collectors.toSet());
      if (!suppliedCodes.equals(MULTI_LAYER_CODES)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "multiLayers must contain exactly layer codes 4, 3, 2 and 1");
      }
      for (StockingLayerDto layer : multiLayers) {
        validateLayerFields(layer, true);
      }
    }
  }

  private void validateLayerFields(StockingLayerDto layer, boolean multi) {
    String code = layer.layerCode();

    if (multi && !LAYER_1_2_ONLY_CODES.contains(code)) {
      if (layer.minResidualBasalArea() != null) {
        rejectLayerField(code, "minResidualBasalArea");
      }
      if (layer.minPostSpacingDensity() != null) {
        rejectLayerField(code, "minPostSpacingDensity");
      }
      if (layer.maxPostSpacingDensity() != null) {
        rejectLayerField(code, "maxPostSpacingDensity");
      }
      if (layer.maxConiferous() != null) {
        rejectLayerField(code, "maxConiferous");
      }
    }
    if (multi && !LAYER_3_4_ONLY_CODES.contains(code) && layer.heightRelativeToComp() != null) {
      rejectLayerField(code, "heightRelativeToComp");
    }

    if (layer.heightRelativeToComp() != null) {
      if (StringUtils.isBlank(layer.heightRelativeToCompUnitCode())) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "heightRelativeToCompUnitCode is required when heightRelativeToComp is supplied (layer "
                + code
                + ")");
      }
      if (!VALID_HEIGHT_RELATIVE_UNIT_CODES.contains(layer.heightRelativeToCompUnitCode())) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "heightRelativeToCompUnitCode must be one of " + VALID_HEIGHT_RELATIVE_UNIT_CODES
                + " (layer " + code + ")");
      }
    }
  }

  private void rejectLayerField(String layerCode, String fieldName) {
    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST, fieldName + " is not applicable to layer " + layerCode);
  }
}
