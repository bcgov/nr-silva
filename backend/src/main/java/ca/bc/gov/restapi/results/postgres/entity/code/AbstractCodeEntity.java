package ca.bc.gov.restapi.results.postgres.entity.code;

import ca.bc.gov.restapi.results.common.entity.BaseCodeEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AttributeOverrides({
    @AttributeOverride(name = "description", column = @Column(name = "description", length = 120, nullable = false)),
    @AttributeOverride(name = "effectiveDate", column = @Column(name = "effective_date", nullable = false)),
    @AttributeOverride(name = "expiryDate", column = @Column(name = "expiry_date", nullable = false)),
    @AttributeOverride(name = "updateTimestamp", column = @Column(name = "update_timestamp", nullable = false))
})
public abstract class AbstractCodeEntity extends BaseCodeEntity {
}
