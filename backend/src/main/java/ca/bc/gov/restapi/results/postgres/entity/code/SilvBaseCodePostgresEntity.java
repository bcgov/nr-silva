package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv base codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "silv_base_code")
@AttributeOverride(name = "code", column = @Column(name = "silv_base_code", length = 2))
public class SilvBaseCodePostgresEntity extends AbstractCodePostgresEntity {}
