package ca.bc.gov.restapi.results.postgres.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.OpeningNotFoundException;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserRecentOpeningRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@DisplayName("Unit Test | UserRecentOpeningService")
class UserRecentOpeningServiceTest {

    @Mock
    private LoggedUserService loggedUserService;

    @Mock
    private UserRecentOpeningRepository userRecentOpeningRepository;

    @Mock
    private OpeningService openingService;

    @Mock
    private OpeningRepository openingRepository;

    @InjectMocks
    private UserRecentOpeningService userRecentOpeningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("storeViewedOpening | new opening | saves entity")
    void storeViewedOpening_newOpening_savesEntity() {
        String userId = "user123";
        Long openingId = 123L;

        when(loggedUserService.getLoggedUserId()).thenReturn(userId);
        when(openingRepository.existsById(openingId)).thenReturn(true);
        when(userRecentOpeningRepository.findByUserIdAndOpeningId(userId, openingId)).thenReturn(Optional.empty());

        UserRecentOpeningDto result = userRecentOpeningService.storeViewedOpening(openingId);

        assertNotNull(result);
        assertEquals(userId, result.userId());
        assertEquals(openingId, result.openingId());

        verify(userRecentOpeningRepository, times(1)).saveAndFlush(any(UserRecentOpeningEntity.class));
    }

    @Test
    @DisplayName("storeViewedOpening | existing opening | updates entity")
    void storeViewedOpening_existingOpening_updatesEntity() {
        String userId = "user123";
        Long openingId = 123L;
        LocalDateTime lastViewed = LocalDateTime.now();
        UserRecentOpeningEntity existingEntity = new UserRecentOpeningEntity(1L, userId, openingId, lastViewed.minusDays(1));

        when(loggedUserService.getLoggedUserId()).thenReturn(userId);
        when(openingRepository.existsById(openingId)).thenReturn(true);
        when(userRecentOpeningRepository.findByUserIdAndOpeningId(userId, openingId)).thenReturn(Optional.of(existingEntity));

        UserRecentOpeningDto result = userRecentOpeningService.storeViewedOpening(openingId);

        assertNotNull(result);
        assertEquals(userId, result.userId());
        assertEquals(openingId, result.openingId());

        verify(userRecentOpeningRepository, times(1)).saveAndFlush(any(UserRecentOpeningEntity.class));
    }

    @Test
    @DisplayName("storeViewedOpening | invalid opening ID | throws exception")
    void storeViewedOpening_invalidOpeningId_throwsException() {
        Long invalidOpeningId = null;

        Exception exception = assertThrows(OpeningNotFoundException.class, () -> {
            userRecentOpeningService.storeViewedOpening(invalidOpeningId);
        });

        assertEquals("Opening ID must contain numbers only!", exception.getMessage());
    }

    @Test
    @DisplayName("storeViewedOpening | opening not found | throws exception")
    void storeViewedOpening_openingIdNotFound_throwsException() {
        Long invalidOpeningId = 7745L;

        when(openingRepository.existsById(invalidOpeningId)).thenReturn(false);

        Exception exception = assertThrows(OpeningNotFoundException.class, () -> {
            userRecentOpeningService.storeViewedOpening(invalidOpeningId);
        });

        assertEquals("404 NOT_FOUND \"UserOpening record(s) not found!\"", exception.getMessage());
    }

    @Test
    void getAllRecentOpeningsForUser_noRecentOpenings_returnsEmptyResult() {
        String userId = "idir@jasgrewa";
        int limit = 10;

        // Arrange
        when(loggedUserService.getLoggedUserId()).thenReturn(userId);
        when(userRecentOpeningRepository.findByUserIdOrderByLastViewedDesc(eq(userId), any(PageRequest.class)))
                .thenReturn(Page.empty()); // Mocking an empty page of recent openings

        // Act
        PaginatedResult<OpeningSearchResponseDto> result = userRecentOpeningService.getAllRecentOpeningsForUser(limit);

        // Assert
        assertNotNull(result);

        // Check if data is null and assert empty
        assertTrue(result.getData() == null || result.getData().isEmpty(), "Data should be empty or null");
    }


    @Test
    void getAllRecentOpeningsForUser_withRecentOpenings_returnsSortedResult() {
        String userId = "user123";
        int limit = 10;
        LocalDateTime now = LocalDateTime.now();
        
        UserRecentOpeningEntity opening1 = new UserRecentOpeningEntity(1L, userId, 123L, now.minusDays(2));
        UserRecentOpeningEntity opening2 = new UserRecentOpeningEntity(2L, userId, 456L, now.minusDays(1));
        
        List<UserRecentOpeningEntity> openings = List.of(opening1, opening2);
        when(loggedUserService.getLoggedUserId()).thenReturn(userId);
        when(userRecentOpeningRepository.findByUserIdOrderByLastViewedDesc(eq(userId), any(PageRequest.class)))
            .thenReturn(new PageImpl<>(openings));

        OpeningSearchResponseDto dto1 = new OpeningSearchResponseDto();
        dto1.setOpeningId(123);

        OpeningSearchResponseDto dto2 = new OpeningSearchResponseDto();
        dto2.setOpeningId(456);

        PaginatedResult<OpeningSearchResponseDto> pageResult = new PaginatedResult<>();
        pageResult.setData(List.of(dto1, dto2));

        when(openingRepository.searchByOpeningIds(any(List.class), any(PageRequest.class)))
            .thenReturn(new PageImpl<>(List.of(dto2, dto1)));
        when(openingService.parsePageResult(any(PaginationParameters.class), any(Page.class)))
            .thenReturn(pageResult);

        PaginatedResult<OpeningSearchResponseDto> result = userRecentOpeningService.getAllRecentOpeningsForUser(limit);

        assertNotNull(result);
        assertEquals(2, result.getData().size());
        assertEquals((long) 456L, (long) result.getData().get(0).getOpeningId());  // Most recent first
        assertEquals((long) 123L, (long) result.getData().get(1).getOpeningId());  // Least recent last
    }
}
