package ca.bc.gov.restapi.results.oracle.entity.opening;

import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * This class represents an Opening in the database.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "OPENING")
@AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "OPENING_ID")),
    @AttributeOverride(name = "status", column = @Column(name = "OPENING_STATUS_CODE", length = 3)),
    @AttributeOverride(name = "category", column = @Column(name = "OPEN_CATEGORY_CODE", length = 7)),
    @AttributeOverride(name = "entryUserId", column = @Column(name = "ENTRY_USERID", length = 30)),
    @AttributeOverride(name = "updateTimestamp", column = @Column(name = "UPDATE_TIMESTAMP")),
    @AttributeOverride(name = "entryTimestamp", column = @Column(name = "ENTRY_TIMESTAMP"))
})
public class OpeningEntity extends BaseOpeningEntity {
}
