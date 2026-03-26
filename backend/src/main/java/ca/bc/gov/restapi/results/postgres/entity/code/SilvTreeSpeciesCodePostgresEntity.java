package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv tree species codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "silv_tree_species_code")
@AttributeOverride(name = "code", column = @Column(name = "silv_tree_species_code", length = 8))
public class SilvTreeSpeciesCodePostgresEntity extends AbstractCodePostgresEntity {}
