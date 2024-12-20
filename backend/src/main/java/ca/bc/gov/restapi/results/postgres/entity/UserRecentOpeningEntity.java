package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
@Entity
@EqualsAndHashCode(exclude = {"id","lastViewed"})
@Table(schema = "silva", name = "user_recent_openings")
public class UserRecentOpeningEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "opening_id", nullable = false)
    private Long openingId;

    @Column(name = "last_viewed", nullable = false)
    private LocalDateTime lastViewed;
}
