package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** This class represents a Client Acronym in the database. */
@Getter
@Setter
@Entity
@ToString
@Table(name = "CLIENT_ACRONYM")
@EqualsAndHashCode
public class ClientAcronymEntity {

  @Id
  @Column(name = "CLIENT_NUMBER", length = 8)
  private String clientNumber;

  @Column(name = "CLIENT_ACRONYM", length = 8)
  private String clientAcronym;
}
