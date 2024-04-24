package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** This class represents a log message in the database, for the oracle extraction flow. */
@Getter
@Setter
@Entity
@Table(name = "oracle_extraction_logs")
@ToString
public class OracleExtractionLogsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "log_message", length = 2000, nullable = false)
  private String logMessage;

  @Column(name = "logged_at")
  private LocalDateTime loggedAt;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "manually_triggered")
  private Boolean manuallyTriggered;

  @PrePersist
  public void setCreatedDateTime() {
    createdAt = LocalDateTime.now();
  }
}
