package ca.bc.gov.restapi.results.oracle.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneOverviewDto;
import ca.bc.gov.restapi.results.oracle.entity.comments.CommentProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewMilestoneProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewOpeningProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneProjection;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvicultureCommentRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Opening Service")
class OpeningServiceTest {

    @Mock
    private OpeningRepository openingRepository;

    @Mock
    private ForestClientService forestClientService;

    @Mock
    private SilvicultureCommentRepository commentRepository;

    private OpeningService openingService;

    private OpeningTombstoneProjection openingTombstone;
    private OpeningTombstoneOverviewOpeningProjection openingOverview;
    private OpeningTombstoneOverviewMilestoneProjection milestoneOverview;
    private ForestClientDto forestClient;
    private CommentProjection commentProjection;

    @BeforeEach
    void setUp() {
        openingService = new OpeningService(openingRepository, forestClientService, commentRepository);

        createTestData();
    }

    private void createTestData() {
        // Create OpeningTombstoneProjection instance
        openingTombstone = new OpeningTombstoneProjection() {
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

        // Create OpeningTombstoneOverviewOpeningProjection instance
        openingOverview = new OpeningTombstoneOverviewOpeningProjection() {
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

        // Create OpeningTombstoneOverviewMilestoneProjection instance
        milestoneOverview = new OpeningTombstoneOverviewMilestoneProjection() {
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

        // Create ForestClientDto instance
        forestClient = ForestClientDto.builder()
                .clientNumber("123456")
                .clientName("Client Name")
                .clientStatusCode(ForestClientStatusEnum.ACTIVE)
                .clientTypeCode(ForestClientTypeEnum.ASSOCIATION)
                .acronym("ABC")
                .build();

        commentProjection = new CommentProjection() {
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

    @Test
    @DisplayName("Get Opening Tombstone with valid data and client number should succeed")
    void getOpeningTombstone_withClientNumber_shouldSucceed() {
        Long openingId = 123L;
        String clientNumber = "123456";

        // Use the pre-created instances
        when(openingRepository.getOpeningTombstoneByOpeningId(openingId))
                .thenReturn(Optional.of(openingTombstone));
        when(forestClientService.getClientByNumber(clientNumber))
                .thenReturn(Optional.of(forestClient));
        when(openingRepository.getOpeningTombstoneOverviewByOpeningId(openingId))
                .thenReturn(Optional.of(openingOverview));
        when(openingRepository.getOpeningTombstoneMilestoneByOpeningId(openingId))
                .thenReturn(Optional.of(milestoneOverview));
        when(commentRepository.getCommentById(openingId))
                .thenReturn(List.of(commentProjection));

        // Execute
        Optional<OpeningDetailsTombstoneOverviewDto> result = openingService.getOpeningTombstone(openingId);

        // Verify
        Assertions.assertTrue(result.isPresent());
        OpeningDetailsTombstoneOverviewDto dto = result.get();

        // Verify OpeningDetailsTombstoneOverviewDto
        Assertions.assertEquals(openingId, dto.openingId());

        // Verify OpeningDetailsTombstoneDto
        Assertions.assertEquals("OPN12345", dto.tombstone().openingNumber());
        Assertions.assertEquals(OpeningStatusEnum.APP, dto.tombstone().openingStatus());
        Assertions.assertEquals("DCC", dto.tombstone().orgUnitCode());
        Assertions.assertEquals("Cariboo-Chilcotin Natural Resource District", dto.tombstone().orgUnitName());
        Assertions.assertEquals(OpeningCategoryEnum.FTML, dto.tombstone().openCategory());
        Assertions.assertEquals("FILE987", dto.tombstone().fileId());
        Assertions.assertEquals("CB001", dto.tombstone().cutBlockID());
        Assertions.assertEquals("CP456", dto.tombstone().cuttingPermitId());
        Assertions.assertEquals("TM7890", dto.tombstone().timberMark());
        Assertions.assertEquals("PUBLIC", dto.tombstone().maxAllowedAccess());
        Assertions.assertEquals(250.5F, dto.tombstone().openingGrossArea());
        Assertions.assertEquals("TESTUSER", dto.tombstone().createdBy());
        Assertions.assertEquals(LocalDate.of(2022, 3, 15), dto.tombstone().createdOn());
        Assertions.assertEquals(LocalDate.of(2023, 5, 20), dto.tombstone().lastUpdatedOn());
        Assertions.assertEquals(LocalDate.of(2022, 6, 1), dto.tombstone().disturbanceStartDate());

        // Verify ForestClientDto within OpeningDetailsTombstoneDto
        Assertions.assertEquals("123456", dto.tombstone().client().clientNumber());
        Assertions.assertEquals("Client Name", dto.tombstone().client().clientName());
        Assertions.assertEquals(ForestClientStatusEnum.ACTIVE, dto.tombstone().client().clientStatusCode());
        Assertions.assertEquals(ForestClientTypeEnum.ASSOCIATION, dto.tombstone().client().clientTypeCode());
        Assertions.assertEquals("ABC", dto.tombstone().client().acronym());

        // Verify OpeningDetailsOverviewDto
        Assertions.assertNotNull(dto.overview());

        // Verify OpeningDetailsOverviewOpeningDto
        Assertions.assertEquals("LIC-123", dto.overview().opening().licenseeId());
        Assertions.assertEquals("FL", dto.overview().opening().tenureType().code());
        Assertions.assertEquals("Forest License", dto.overview().opening().tenureType().description());
        Assertions.assertEquals("TSA", dto.overview().opening().managementUnitType().code());
        Assertions.assertEquals("Timber Supply Area", dto.overview().opening().managementUnitType().description());
        Assertions.assertEquals("TSA01", dto.overview().opening().managementUnitId());
        Assertions.assertEquals("TSO456", dto.overview().opening().timberSaleOffice().code());
        Assertions.assertEquals("Williams Lake Timber Sales Office", dto.overview().opening().timberSaleOffice().description());

        // Verify comments within OpeningDetailsOverviewOpeningDto
        Assertions.assertNotNull(dto.overview().opening().comments());
        Assertions.assertEquals(1, dto.overview().opening().comments().size());
        CommentDto comment = dto.overview().opening().comments().get(0);
        Assertions.assertEquals("SRC001", comment.commentSource().code());
        Assertions.assertEquals("Source Name", comment.commentSource().description());
        Assertions.assertEquals("TYPE001", comment.commentType().code());
        Assertions.assertEquals("Type Name", comment.commentType().description());
        Assertions.assertEquals("This is a test comment.", comment.commentText());

        // Verify OpeningDetailsOverviewMilestoneDto
        Assertions.assertEquals("SU123", dto.overview().milestones().standardsUnitId());
        Assertions.assertEquals(LocalDate.of(2022, 10, 15), dto.overview().milestones().postHarvestDeclaredDate());
        Assertions.assertEquals(LocalDate.of(2023, 11, 30), dto.overview().milestones().regenDeclaredDate());
        Assertions.assertEquals(7, dto.overview().milestones().regenOffsetYears());
        Assertions.assertEquals(LocalDate.of(2029, 6, 1), dto.overview().milestones().regenDueDate());
        Assertions.assertEquals(LocalDate.of(2032, 8, 15), dto.overview().milestones().freeGrowingDeclaredDate());
        Assertions.assertEquals(15, dto.overview().milestones().freeGrowingOffsetYears());
        Assertions.assertEquals(LocalDate.of(2037, 6, 1), dto.overview().milestones().freeGrowingDueDate());
    }

    @Test
    @DisplayName("Get Opening Tombstone with no data should return empty")
    void getOpeningTombstone_noData_shouldReturnEmpty() {
        Long openingId = 1L;
        when(openingRepository.getOpeningTombstoneByOpeningId(anyLong()))
                .thenReturn(Optional.empty());

        Optional<OpeningDetailsTombstoneOverviewDto> result = openingService.getOpeningTombstone(openingId);

        Assertions.assertTrue(result.isEmpty());
    }

    
}
