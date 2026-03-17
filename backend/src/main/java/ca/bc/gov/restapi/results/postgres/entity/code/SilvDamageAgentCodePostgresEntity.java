package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv damage agent codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "silv_damage_agent_code")
@AttributeOverride(name = "code", column = @Column(name = "silv_damage_agent_code", length = 3))
public class SilvDamageAgentCodePostgresEntity extends AbstractCodePostgresEntity {}
