package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/** This class represents a record in the database for the openings_last_year table. */
@Getter
@Setter
@Entity
@Table(name = "openings_last_year")
public class OpeningsLastYearEntity {

  @Id
  @Column(name = "opening_id")
  private String openingId;

  @Column(name = "opening_entry_userid", nullable = false)
  private String userId;

  @Column(name = "entry_timestamp", nullable = false)
  private LocalDateTime entryTimestamp;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;

  @Column(name = "status_code", nullable = false)
  private String status;

  @Column(name = "org_unit_code", nullable = false)
  private String orgUnitCode;
}
