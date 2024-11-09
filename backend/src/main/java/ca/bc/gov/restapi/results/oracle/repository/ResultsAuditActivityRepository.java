package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.ResultsAuditEventProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResultsAuditActivityRepository extends JpaRepository<OpeningEntity, Long> {

  @Query(
      value = """
          SELECT
            rae.results_audit_event_id,
            rae.opening_id,
            rae.results_audit_action_code as action_code,
            raac.description as action_code_description,
            o.open_category_code as category_code,
            occ.description as category_code_description,
            rae.entry_timestamp,
            rae.entry_userid
          FROM THE.RESULTS_AUDIT_EVENT rae
          LEFT JOIN THE.RESULTS_AUDIT_ACTION_CODE raac ON raac.RESULTS_AUDIT_ACTION_CODE = rae.RESULTS_AUDIT_ACTION_CODE
          LEFT JOIN THE.OPENING o ON o.OPENING_ID = rae.OPENING_ID
          LEFT JOIN THE.OPEN_CATEGORY_CODE occ ON occ.OPEN_CATEGORY_CODE = o.OPEN_CATEGORY_CODE
          WHERE rae.ENTRY_USERID = ?1
          ORDER BY rae.ENTRY_TIMESTAMP DESC
          FETCH FIRST ?2 ROWS ONLY""",
      nativeQuery = true)
  List<ResultsAuditEventProjection> findUserRecentAuditEvents(String userId, Integer limit);


}
