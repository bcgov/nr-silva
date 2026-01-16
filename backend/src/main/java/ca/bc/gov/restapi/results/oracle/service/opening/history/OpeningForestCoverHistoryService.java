package ca.bc.gov.restapi.results.oracle.service.opening.history;

import ca.bc.gov.restapi.results.common.repository.ForestCoverRepository;
import ca.bc.gov.restapi.results.common.service.opening.history.impl.AbstractOpeningForestCoverHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OpeningForestCoverHistoryService extends AbstractOpeningForestCoverHistoryService {

    public OpeningForestCoverHistoryService(ForestCoverRepository coverRepository) {
        super(coverRepository);
    }
}
