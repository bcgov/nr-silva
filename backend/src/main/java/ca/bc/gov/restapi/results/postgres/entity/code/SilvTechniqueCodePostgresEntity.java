package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv technique codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "silv_technique_code")
@AttributeOverride(name = "code", column = @Column(name = "silv_technique_code", length = 2))
public class SilvTechniqueCodePostgresEntity extends AbstractCodePostgresEntity {}
