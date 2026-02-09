package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** This class represents an Opening Category in the database. */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "OPEN_CATEGORY_CODE")
@AttributeOverride(name = "code", column = @Column(name = "OPEN_CATEGORY_CODE", length = 3))
public class OpenCategoryCodeOracleEntity extends AbstractCodeOracleEntity {}
