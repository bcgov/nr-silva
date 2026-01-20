package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "results_electronic_submission")
public class ResultsElectronicSubmissionEntity {

  @Id
  @Column(name = "results_submission_id")
  private Long resultsSubmissionId;

  @Column(name = "client_number", length = 8)
  private String clientNumber;

  @Column(name = "client_location_code", length = 2)
  private String clientLocationCode;

  @Column(name = "org_unit_no")
  private Long orgUnitNo;
}
