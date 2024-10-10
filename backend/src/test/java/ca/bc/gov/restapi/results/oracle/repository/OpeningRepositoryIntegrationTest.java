package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class OpeningRepositoryIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private OpeningRepository openingRepository;

  @Test
  @DisplayName("Finds all openings by entry user ID")
  void findAllByEntryUserIdTest() {
    String entryUserId = "idir-here";
    Pageable pageable = PageRequest.of(0, 5);

    Page<OpeningEntity> openingPage = openingRepository.findAllByEntryUserId(entryUserId, pageable);

    Assertions.assertNotNull(openingPage.getContent());
    Assertions.assertEquals(1, openingPage.getContent().size());
    Assertions.assertEquals(entryUserId, openingPage.getContent().get(0).getEntryUserId());
  }
}
