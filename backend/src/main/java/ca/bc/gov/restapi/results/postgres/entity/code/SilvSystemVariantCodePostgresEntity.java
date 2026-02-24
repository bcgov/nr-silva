package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv system variant codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "silv_system_variant_code")
@AttributeOverride(name = "code", column = @Column(name = "silv_system_variant_code", length = 3))
public class SilvSystemVariantCodePostgresEntity extends AbstractCodePostgresEntity {}
