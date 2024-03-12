package ca.bc.gov.restapi.results.repository.oracle;

import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:sql_scripts/OpeningRepositoryIntegrationTest.sql"})
class OpeningRepositoryIntegrationTest {

  @Autowired private OpeningRepository openingRepository;

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
