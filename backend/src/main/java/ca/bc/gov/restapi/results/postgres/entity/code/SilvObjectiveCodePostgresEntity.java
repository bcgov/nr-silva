package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv objective codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "silv_objective_code")
@AttributeOverride(name = "code", column = @Column(name = "silv_objective_code", length = 3))
public class SilvObjectiveCodePostgresEntity extends AbstractCodeEntity {}
