package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for stocking type codes (Oracle). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "STOCKING_TYPE_CODE")
@AttributeOverride(name = "code", column = @Column(name = "STOCKING_TYPE_CODE", length = 3))
public class StockingTypeCodeOracleEntity extends AbstractCodeOracleEntity {}
