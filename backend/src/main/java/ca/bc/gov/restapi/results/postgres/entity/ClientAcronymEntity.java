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
@Table(schema = "silva", name = "client_acronym")
public class ClientAcronymEntity {
  @Id
  @Column(name = "client_number", length = 8)
  private String clientNumber;

  @Column(name = "client_acronym", length = 8)
  private String clientAcronym;
}
