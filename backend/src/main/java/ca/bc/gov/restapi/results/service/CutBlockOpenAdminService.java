package ca.bc.gov.restapi.results.service;

import ca.bc.gov.restapi.results.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.repository.CutBlockOpenAdminRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CutBlockOpenAdminService {

  private final CutBlockOpenAdminRepository cutBlockOpenAdminRepository;

  public List<CutBlockOpenAdminEntity> findAllByOpeningIdIn(List<Long> openingIdList) {
    return cutBlockOpenAdminRepository.findAllByOpeningIdIn(openingIdList);
  }
}
