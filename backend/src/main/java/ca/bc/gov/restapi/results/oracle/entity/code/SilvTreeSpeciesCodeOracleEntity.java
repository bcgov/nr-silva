package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv tree species codes (Oracle). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "SILV_TREE_SPECIES_CODE")
@AttributeOverride(name = "code", column = @Column(name = "SILV_TREE_SPECIES_CODE", length = 8))
public class SilvTreeSpeciesCodeOracleEntity extends AbstractCodeOracleEntity {}
