package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for disturbance codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "disturbance_code")
@AttributeOverride(name = "code", column = @Column(name = "disturbance_code", length = 3))
public class DisturbanceCodePostgresEntity extends AbstractCodePostgresEntity {}
