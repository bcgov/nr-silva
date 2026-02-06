package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** This class represents an Opening Category in the database. */
@SuperBuilder
@NoArgsConstructor
@With
@Entity
@Table(schema = "silva", name = "open_category_code")
@AttributeOverride(name = "code", column = @Column(name = "open_category_code", length = 3))
public class OpenCategoryCodeEntity extends AbstractCodeEntity {}
