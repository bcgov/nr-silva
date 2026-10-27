package ca.bc.gov.restapi.results.postgres.security;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@Transactional
@DisplayName("Integration Test | Opening client resolver")
class OpeningClientResolverIntegrationTest extends AbstractTestContainerIntegrationTest {

  // Opening with forest file TFL47 (licensee 00010003) and cut block 189945, no O/R clients
  private static final Long OPENING_ID = 1004185L;
  private static final long CUT_BLOCK = 189945L;
  // Opening without any client
  private static final Long OPENING_WITHOUT_CLIENTS = 1480130L;

  @Autowired private OpeningClientResolver resolver;
  @Autowired private JdbcTemplate jdbcTemplate;

  private void addCutBlockClient(long key, String type, String clientNumber) {
    jdbcTemplate.update(
        "INSERT INTO silva.cut_block_client (cblk_client_skey, cb_skey, "
            + "cut_block_client_type_code, client_locn_code, client_number, revision_count, "
            + "entry_userid, entry_timestamp, update_userid, update_timestamp) "
            + "VALUES (?, ?, ?, '00', ?, 1, 'TEST', now(), 'TEST', now())",
        key,
        CUT_BLOCK,
        type,
        clientNumber);
  }

  private void addForestFileClient(long key, String type, String clientNumber) {
    jdbcTemplate.update(
        "INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, "
            + "client_number, client_locn_code, forest_file_client_type_code, revision_count, "
            + "entry_userid, entry_timestamp, update_userid, update_timestamp) "
            + "VALUES (?, 'TFL47', ?, '00', ?, 1, 'TEST', now(), 'TEST', now())",
        key,
        clientNumber,
        type);
  }

  @Test
  @DisplayName("Returns the licensee of the forest file")
  void clientNumbersOf_licensee_returned() {
    assertThat(resolver.clientNumbersOf(OPENING_ID)).containsExactly("00010003");
  }

  @Test
  @DisplayName("Adds the obligation and responsibility holders of the cut block")
  void clientNumbersOf_obligationAndResponsibility_added() {
    addCutBlockClient(90000001L, "O", "00010004");
    addCutBlockClient(90000002L, "R", "00010005");

    assertThat(resolver.clientNumbersOf(OPENING_ID))
        .containsExactlyInAnyOrder("00010003", "00010004", "00010005");
  }

  @Test
  @DisplayName("Ignores other client types")
  void clientNumbersOf_otherTypes_ignored() {
    addCutBlockClient(90000003L, "D", "00010006");
    addForestFileClient(90000004L, "C", "00010008");

    assertThat(resolver.clientNumbersOf(OPENING_ID)).isEqualTo(Set.of("00010003"));
  }

  @Test
  @DisplayName("Returns each client once when it holds both obligation and responsibility")
  void clientNumbersOf_sameClientTwice_returnedOnce() {
    addCutBlockClient(90000005L, "O", "00010004");
    addCutBlockClient(90000006L, "R", "00010004");

    assertThat(resolver.clientNumbersOf(OPENING_ID))
        .containsExactlyInAnyOrder("00010003", "00010004");
  }

  @Test
  @DisplayName("Returns nothing for an opening without clients")
  void clientNumbersOf_openingWithoutClients_empty() {
    assertThat(resolver.clientNumbersOf(OPENING_WITHOUT_CLIENTS)).isEmpty();
  }
}
