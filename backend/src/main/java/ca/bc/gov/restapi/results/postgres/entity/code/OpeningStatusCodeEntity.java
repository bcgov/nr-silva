package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** This class represents an Opening Status Code in the database. */
@SuperBuilder
@NoArgsConstructor
@With
@Entity
@Table(schema = "silva", name = "opening_status_code")
@AttributeOverride(name = "code", column = @Column(name = "opening_status_code", length = 3))
public class OpeningStatusCodeEntity extends AbstractCodeEntity{
}
