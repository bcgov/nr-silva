package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserRecentOpeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRecentOpeningServiceTest {

    @Mock
    private LoggedUserService loggedUserService;

    @Mock
    private UserRecentOpeningRepository userRecentOpeningRepository;

    @Mock
    private OpeningService openingService;

    @InjectMocks
    private UserRecentOpeningService userRecentOpeningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void storeViewedOpening_newOpening_savesEntity() {
        String userId = "user123";
        String openingId = "123";
        LocalDateTime lastViewed = LocalDateTime.now();

        when(loggedUserService.getLoggedUserId()).thenReturn(userId);
        when(userRecentOpeningRepository.findByUserIdAndOpeningId(userId, openingId)).thenReturn(null);

        UserRecentOpeningDto result = userRecentOpeningService.storeViewedOpening(openingId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(openingId, result.getOpeningId());

        verify(userRecentOpeningRepository, times(1)).save(any(UserRecentOpeningEntity.class));
    }

    @Test
    void storeViewedOpening_existingOpening_updatesEntity() {
        String userId = "user123";
        String openingId = "123";
        LocalDateTime lastViewed = LocalDateTime.now();
        UserRecentOpeningEntity existingEntity = new UserRecentOpeningEntity(1L, userId, openingId, lastViewed.minusDays(1));

        when(loggedUserService.getLoggedUserId()).thenReturn(userId);
        when(userRecentOpeningRepository.findByUserIdAndOpeningId(userId, openingId)).thenReturn(existingEntity);

        UserRecentOpeningDto result = userRecentOpeningService.storeViewedOpening(openingId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(openingId, result.getOpeningId());

        verify(userRecentOpeningRepository, times(1)).save(existingEntity);
    }

    @Test
    void storeViewedOpening_invalidOpeningId_throwsException() {
        String invalidOpeningId = "abc";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userRecentOpeningService.storeViewedOpening(invalidOpeningId);
        });

        assertEquals("Opening ID must contain numbers only!", exception.getMessage());
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
        
        UserRecentOpeningEntity opening1 = new UserRecentOpeningEntity(1L, userId, "123", now.minusDays(2));
        UserRecentOpeningEntity opening2 = new UserRecentOpeningEntity(2L, userId, "456", now.minusDays(1));
        
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

        when(openingService.openingSearch(any(OpeningSearchFiltersDto.class), any(PaginationParameters.class)))
            .thenReturn(pageResult);

        PaginatedResult<OpeningSearchResponseDto> result = userRecentOpeningService.getAllRecentOpeningsForUser(limit);

        assertNotNull(result);
        assertEquals(2, result.getData().size());
        assertEquals((long) 456L, (long) result.getData().get(0).getOpeningId());  // Most recent first
        assertEquals((long) 123L, (long) result.getData().get(1).getOpeningId());  // Least recent last
    }
}
