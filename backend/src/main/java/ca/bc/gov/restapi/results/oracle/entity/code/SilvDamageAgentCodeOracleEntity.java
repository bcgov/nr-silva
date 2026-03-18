package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv damage agent codes (Oracle). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "SILV_DAMAGE_AGENT_CODE")
@AttributeOverride(name = "code", column = @Column(name = "SILV_DAMAGE_AGENT_CODE", length = 3))
public class SilvDamageAgentCodeOracleEntity extends AbstractCodeOracleEntity {}
