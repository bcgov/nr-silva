package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** This class holds methods for fetching and handling {@link OpeningEntity} in general. */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningService {

  private final OpeningRepository openingRepository;
  private final ForestClientService forestClientService;
  private final SilvicultureCommentRepository commentRepository;
}
