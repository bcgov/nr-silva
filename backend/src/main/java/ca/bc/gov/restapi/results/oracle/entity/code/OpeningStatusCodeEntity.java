package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for opening status codes. */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "OPENING_STATUS_CODE")
@AttributeOverride(name = "code", column = @Column(name = "OPENING_STATUS_CODE", length = 3))
public class OpeningStatusCodeEntity extends AbstractCodeEntity {}
