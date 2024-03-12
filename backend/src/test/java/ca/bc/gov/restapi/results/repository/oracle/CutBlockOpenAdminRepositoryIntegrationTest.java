package ca.bc.gov.restapi.results.repository.oracle;

import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.repository.CutBlockOpenAdminRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:sql_scripts/CutBlockOpenAdminRepositoryIntegrationTest.sql"})
class CutBlockOpenAdminRepositoryIntegrationTest {

  @Autowired private CutBlockOpenAdminRepository cutBlockOpenAdminRepository;

  @Test
  @DisplayName("find all openings given a list of opening ids")
  void findAllByOpeningIdIn() {
    List<Long> openingIdList = List.of(101L, 102L);

    List<CutBlockOpenAdminEntity> cutBlocks =
        cutBlockOpenAdminRepository.findAllByOpeningIdIn(openingIdList);

    Assertions.assertNotNull(cutBlocks);
    Assertions.assertEquals(2, cutBlocks.size());
  }
}
