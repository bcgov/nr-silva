package ca.bc.gov.restapi.results.postgres.entity.opening;

import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import jakarta.persistence.AttributeOverride;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@With
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "opening")
@AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "opening_id")),
    @AttributeOverride(name = "status", column = @Column(name = "opening_status_code", length = 3)),
    @AttributeOverride(name = "category", column = @Column(name = "open_category_code", length = 7)),
    @AttributeOverride(name = "entryUserId", column = @Column(name = "entry_userid", length = 30)),
    @AttributeOverride(name = "updateTimestamp", column = @Column(name = "update_timestamp")),
    @AttributeOverride(name = "entryTimestamp", column = @Column(name = "entry_timestamp"))
})
public class OpeningEntity extends BaseOpeningEntity {}
