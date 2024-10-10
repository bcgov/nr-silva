package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

/** This class represents an Electronic Submission for the Openings in the database. */
@Data
@Builder
@With
@Entity
@Table(name = "RESULTS_ELECTRONIC_SUBMISSION")
public class ResultsElectronicSubmissionEntity {
  
  @Id
  @Column(name = "RESULTS_SUBMISSION_ID")
  private Long resultsSubmissionId;

  @Column(name = "CLIENT_NUMBER", length = 8)
  private String clientNumber;

  @Column(name = "CLIENT_LOCATION_CODE", length = 2)
  private String clientLocationCode;

  @Column(name = "ORG_UNIT_NO")
  private Long orgUnitNo;
}
