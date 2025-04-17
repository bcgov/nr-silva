package ca.bc.gov.restapi.results.oracle.util;

import java.time.LocalDate;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.oracle.entity.comments.CommentProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewMilestoneProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewOpeningProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneProjection;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;

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
            public OpeningStatusEnum getOpeningStatus() {
                return OpeningStatusEnum.APP;
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
            public OpeningCategoryEnum getOpenCategory() {
                return OpeningCategoryEnum.FTML;
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
}
