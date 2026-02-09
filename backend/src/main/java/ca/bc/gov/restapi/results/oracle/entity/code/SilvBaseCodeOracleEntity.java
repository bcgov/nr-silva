package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv base codes (Oracle). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "SILV_BASE_CODE")
@AttributeOverride(name = "code", column = @Column(name = "SILV_BASE_CODE", length = 2))
public class SilvBaseCodeOracleEntity extends AbstractCodeOracleEntity {}
