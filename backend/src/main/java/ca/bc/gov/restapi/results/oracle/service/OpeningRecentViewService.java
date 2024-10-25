package ca.bc.gov.restapi.results.oracle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRecentViewRepository;

import java.util.List;

@Service
public class OpeningRecentViewService {

    @Autowired
    private OpeningRecentViewRepository openingRecentViewRepository;

    public PaginatedResult<OpeningSearchResponseDto> getOpeningsByIds(List<String> openingIds) {
        PaginationParameters pagination = new PaginationParameters(0, 10);
        return openingRecentViewRepository.getUserRecentOpenings(openingIds, pagination);
    }
}
