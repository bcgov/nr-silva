package ca.bc.gov.restapi.results.oracle.service.opening.history;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.opening.history.impl.AbstractOpeningStandardUnitHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OpeningStandardUnitHistoryService extends AbstractOpeningStandardUnitHistoryService {

    public OpeningStandardUnitHistoryService(OpeningRepository openingRepository,
        SilvicultureCommentRepository commentRepository) {
        super(openingRepository, commentRepository);
    }
}
