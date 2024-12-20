package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * This class represents a Client Acronym in the database.
 */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "THE", name = "CLIENT_ACRONYM")
public class ClientAcronymEntity {

  @Id
  @Column(name = "CLIENT_NUMBER", length = 8)
  private String clientNumber;

  @Column(name = "CLIENT_ACRONYM", length = 8)
  private String clientAcronym;
}
