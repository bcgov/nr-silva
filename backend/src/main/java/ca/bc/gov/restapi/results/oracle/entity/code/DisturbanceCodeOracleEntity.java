package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for disturbance codes (Oracle). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "DISTURBANCE_CODE")
@AttributeOverride(name = "code", column = @Column(name = "DISTURBANCE_CODE", length = 3))
public class DisturbanceCodeOracleEntity extends AbstractCodeOracleEntity {}
