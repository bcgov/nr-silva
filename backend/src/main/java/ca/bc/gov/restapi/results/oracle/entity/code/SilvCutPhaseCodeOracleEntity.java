package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv cut phase codes (Oracle). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "SILV_CUT_PHASE_CODE")
@AttributeOverride(name = "code", column = @Column(name = "SILV_CUT_PHASE_CODE", length = 5))
public class SilvCutPhaseCodeOracleEntity extends AbstractCodeOracleEntity {}
