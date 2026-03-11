package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for stocking status codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "stocking_status_code")
@AttributeOverride(name = "code", column = @Column(name = "stocking_status_code", length = 3))
public class StockingStatusCodePostgresEntity extends AbstractCodePostgresEntity {}
