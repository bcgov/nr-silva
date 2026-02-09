package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv method codes (Oracle). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "SILV_METHOD_CODE")
@AttributeOverride(name = "code", column = @Column(name = "SILV_METHOD_CODE", length = 5))
public class SilvMethodCodeOracleEntity extends AbstractCodeOracleEntity {}
