package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv cut phase codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "silv_cut_phase_code")
@AttributeOverride(name = "code", column = @Column(name = "silv_cut_phase_code", length = 5))
public class SilvCutPhaseCodePostgresEntity extends AbstractCodePostgresEntity {}
