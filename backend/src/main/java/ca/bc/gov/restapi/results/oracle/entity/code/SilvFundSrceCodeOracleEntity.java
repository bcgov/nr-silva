package ca.bc.gov.restapi.results.oracle.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv fund source codes (Oracle). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "SILV_FUND_SRCE_CODE")
@AttributeOverride(name = "code", column = @Column(name = "SILV_FUND_SRCE_CODE", length = 3))
public class SilvFundSrceCodeOracleEntity extends AbstractCodeEntity {}
