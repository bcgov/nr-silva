package ca.bc.gov.restapi.results.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.bc.gov.restapi.results.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.repository.CutBlockOpenAdminRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CutBlockOpenAdminService {

  private final CutBlockOpenAdminRepository cutBlockOpenAdminRepository;
  
  public List<CutBlockOpenAdminEntity> getAllByOpeningId(Long openingId) {
    return cutBlockOpenAdminRepository.findAllByOpeningId(openingId);
  }
}
