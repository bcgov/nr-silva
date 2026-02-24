package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv system codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "silv_system_code")
@AttributeOverride(name = "code", column = @Column(name = "silv_system_code", length = 5))
public class SilvSystemCodePostgresEntity extends AbstractCodePostgresEntity {}
