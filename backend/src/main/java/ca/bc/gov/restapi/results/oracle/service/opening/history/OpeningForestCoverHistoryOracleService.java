package ca.bc.gov.restapi.results.oracle.service.opening.history;

import ca.bc.gov.restapi.results.common.repository.ForestCoverRepository;
import ca.bc.gov.restapi.results.common.service.opening.history.impl.AbstractOpeningForestCoverHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class OpeningForestCoverHistoryOracleService extends AbstractOpeningForestCoverHistoryService {

    public OpeningForestCoverHistoryOracleService(ForestCoverRepository coverRepository) {
        super(coverRepository);
    }
}
