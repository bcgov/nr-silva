package ca.bc.gov.restapi.results.oracle.util;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.oracle.entity.comments.CommentProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingLayerProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingSpeciesProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewMilestoneProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewOpeningProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneProjection;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.time.LocalDate;

public class OpeningTestDataFactory {

  public static OpeningTombstoneProjection createTombstoneProjection() {
    return new OpeningTombstoneProjection() {
      @Override
      public Long getOpeningId() {
        return 123L;
      }

      @Override
      public String getOpeningNumber() {
        return "OPN12345";
      }

      @Override
      public String getOpeningStatusCode() {
        return OpeningStatusEnum.APP.getCode();
      }

      @Override
      public String getOpeningStatusName() {
        return OpeningStatusEnum.APP.getDescription();
      }

      @Override
      public String getOrgUnitCode() {
        return "DCC";
      }

      @Override
      public String getOrgUnitName() {
        return "Cariboo-Chilcotin Natural Resource District";
      }

      @Override
      public String getOpenCategoryCode() {
        return OpeningCategoryEnum.FTML.getCode();
      }

      @Override
      public String getOpenCategoryName() {
        return OpeningCategoryEnum.FTML.getDescription();
      }

      @Override
      public String getClient() {
        return "123456";
      }

      @Override
      public String getFileId() {
        return "FILE987";
      }

      @Override
      public String getCutBlockID() {
        return "CB001";
      }

      @Override
      public String getCuttingPermitId() {
        return "CP456";
      }

      @Override
      public String getTimberMark() {
        return "TM7890";
      }

      @Override
      public String getMaxAllowedAccess() {
        return "PUBLIC";
      }

      @Override
      public Float getOpeningGrossArea() {
        return 250.5F;
      }

      @Override
      public String getCreatedBy() {
        return "TESTUSER";
      }

      @Override
      public LocalDate getCreatedOn() {
        return LocalDate.of(2022, 3, 15);
      }

      @Override
      public LocalDate getLastUpdatedOn() {
        return LocalDate.of(2023, 5, 20);
      }

      @Override
      public LocalDate getDisturbanceStartDate() {
        return LocalDate.of(2022, 6, 1);
      }
    };
  }

  public static OpeningTombstoneOverviewOpeningProjection createTombstoneOverviewOpeningProjection() {
    return new OpeningTombstoneOverviewOpeningProjection() {
      @Override
      public String getLicenseeOpeningId() {
        return "LIC-123";
      }

      @Override
      public String getTenureTypeCode() {
        return "FL";
      }

      @Override
      public String getTenureTypeName() {
        return "Forest License";
      }

      @Override
      public String getManagementUnitTypeCode() {
        return "TSA";
      }

      @Override
      public String getManagementUnitTypeName() {
        return "Timber Supply Area";
      }

      @Override
      public String getManagementUnitId() {
        return "TSA01";
      }

      @Override
      public String getTimberSaleOfficeCode() {
        return "TSO456";
      }

      @Override
      public String getTimberSaleOfficeName() {
        return "Williams Lake Timber Sales Office";
      }
    };
  }

  public static OpeningTombstoneOverviewMilestoneProjection createTombstoneOverviewMilestoneProjection() {
    return new OpeningTombstoneOverviewMilestoneProjection() {
      @Override
      public String getStandardsUnitId() {
        return "SU123";
      }

      @Override
      public LocalDate getPostHarvestDeclaredDate() {
        return LocalDate.of(2022, 10, 15);
      }

      @Override
      public LocalDate getRegenDeclaredDate() {
        return LocalDate.of(2023, 11, 30);
      }

      @Override
      public Integer getRegenOffsetYears() {
        return 7;
      }

      @Override
      public LocalDate getRegenDueDate() {
        return LocalDate.of(2029, 6, 1);
      }

      @Override
      public LocalDate getFreeGrowingDeclaredDate() {
        return LocalDate.of(2032, 8, 15);
      }

      @Override
      public Integer getFreeGrowingOffsetYears() {
        return 15;
      }

      @Override
      public LocalDate getFreeGrowingDueDate() {
        return LocalDate.of(2037, 6, 1);
      }
    };
  }

  public static ForestClientDto createForestClientDto() {
    return ForestClientDto.builder()
        .clientNumber("123456")
        .clientName("Client Name")
        .clientStatusCode(ForestClientStatusEnum.ACTIVE)
        .clientTypeCode(ForestClientTypeEnum.ASSOCIATION)
        .acronym("ABC")
        .build();
  }

  public static CommentProjection createCommentProjection() {
    return new CommentProjection() {
      @Override
      public String getCommentSourceCode() {
        return "SRC001";
      }

      @Override
      public String getCommentSourceName() {
        return "Source Name";
      }

      @Override
      public String getCommentTypeCode() {
        return "TYPE001";
      }

      @Override
      public String getCommentTypeName() {
        return "Type Name";
      }

      @Override
      public String getCommentText() {
        return "This is a test comment.";
      }
    };
  }

  public static OpeningStockingDetailsProjection createOpeningStockingDetailsProjection() {
    return new OpeningStockingDetailsProjection() {
      @Override
      public String getStockingStandardUnit() {
        return "A";
      }

      @Override
      public Long getSsid() {
        return 1013720L;
      }

      @Override
      public Boolean getDefaultMof() {
        return Boolean.FALSE;
      }

      @Override
      public Boolean getManualEntry() {
        return Boolean.FALSE;
      }

      @Override
      public Long getFspId() {
        return null;
      }

      @Override
      public Float getNetArea() {
        return 25.5F;
      }

      @Override
      public Float getSoilDisturbancePercent() {
        return 5.0F;
      }

      @Override
      public String getBecZoneCode() {
        return "CWH";
      }

      @Override
      public String getBecSubzoneCode() {
        return "vm";
      }

      @Override
      public String getBecVariant() {
        return "1";
      }

      @Override
      public String getBecPhase() {
        return null;
      }

      @Override
      public String getBecSiteSeries() {
        return "01";
      }

      @Override
      public String getBecSiteType() {
        return null;
      }

      @Override
      public String getBecSeral() {
        return null;
      }

      @Override
      public Long getRegenDelay() {
        return 6L;
      }

      @Override
      public Long getFreeGrowingLate() {
        return 14L;
      }

      @Override
      public Long getFreeGrowingEarly() {
        return 11L;
      }

      @Override
      public String getAdditionalStandards() {
        return
            "(ALL625) ; ALL SPECIES - minimum inter-tree spacing within the net area to be reforested "
            +
            "may be reduced to 1.5 metres to allow the selection of the most productive microsites in "
            +
            "areas of poor plantability due to hygric conditions and bedrock areas.; (ALL626) ; " +
            "ALL SPECIES - minimum inter-tree spacing within 20 metres of either site of road centreline "
            +
            "may be reduced to 1 metre where road construction or harvesting activities have created s "
            +
            "oil disturbance and reduced plantability.; (FDC628) ; FDC - is a preferred species on steep "
            +
            "southerly aspects (SE to SW) or on southerly, westerly and easterly slopes where pre-harvest "
            +
            "stand composition of Fd is >20% by merchantable volume.";
      }
    };
  }

  public static OpeningStockingSpeciesProjection createPreferredSpeciesProjection() {
    return new OpeningStockingSpeciesProjection() {
      @Override
      public String getSpeciesCode() {
        return "CW";
      }

      @Override
      public String getSpeciesName() {
        return "western redcedar";
      }

      @Override
      public Long getMinHeight() {
        return 1L;
      }
    };
  }

  public static OpeningStockingSpeciesProjection createAcceptableSpeciesProjection() {
    return new OpeningStockingSpeciesProjection() {
      @Override
      public String getSpeciesCode() {
        return "BA";
      }

      @Override
      public String getSpeciesName() {
        return "amabilis fir";
      }

      @Override
      public Long getMinHeight() {
        return 1L;
      }
    };
  }

  public static OpeningStockingLayerProjection createStockingLayerProjection() {
    return new OpeningStockingLayerProjection() {
      @Override
      public Long getMinWellspacedTrees() {
        return 500L;
      }

      @Override
      public Long getMinPreferredWellspacedTrees() {
        return 400L;
      }

      @Override
      public Long getMinHorizontalDistanceWellspacedTrees() {
        return 2L;
      }

      @Override
      public Long getTargetWellspacedTrees() {
        return 900L;
      }

      @Override
      public Long getMinResidualBasalArea() {
        return null;
      }

      @Override
      public Long getMinPostspacingDensity() {
        return 800L;
      }

      @Override
      public Long getMaxPostspacingDensity() {
        return 2000L;
      }

      @Override
      public Long getMaxConiferous() {
        return 10000L;
      }

      @Override
      public Long getHeightRelativeToComp() {
        return 150L;
      }
    };
  }
}
