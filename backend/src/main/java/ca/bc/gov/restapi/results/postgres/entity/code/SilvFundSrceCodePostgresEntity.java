package ca.bc.gov.restapi.results.postgres.entity.code;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for silv fund source codes (Postgres). */
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "silva", name = "silv_fund_srce_code")
@AttributeOverride(name = "code", column = @Column(name = "silv_fund_srce_code", length = 3))
public class SilvFundSrceCodePostgresEntity extends AbstractCodeEntity {}
