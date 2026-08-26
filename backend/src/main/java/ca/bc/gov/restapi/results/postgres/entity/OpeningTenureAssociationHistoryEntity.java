package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

/** Immutable record of an opening-to-tenure association change. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "opening_tenure_association_history")
public class OpeningTenureAssociationHistoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "opening_tenure_association_history_id")
  private Long id;

  @Column(name = "cut_block_open_admin_id", nullable = false)
  private Long cutBlockOpenAdminId;

  @Column(name = "opening_id", nullable = false)
  private Long openingId;

  @Column(name = "action", nullable = false, length = 12)
  private String action;

  @Column(name = "forest_file_id", length = 10)
  private String forestFileId;

  @Column(name = "cutting_permit_id", length = 3)
  private String cuttingPermitId;

  @Column(name = "cut_block_id", length = 10)
  private String cutBlockId;

  @Column(name = "timber_mark", length = 10)
  private String timberMark;

  @Column(name = "opening_prime_licence_ind", length = 1)
  private String openingPrimeLicenceInd;

  @Column(name = "revision_count", nullable = false)
  private Integer revisionCount;

  @Column(name = "event_userid", nullable = false, length = 30)
  private String eventUserid;

  @Column(name = "event_timestamp", nullable = false)
  private LocalDateTime eventTimestamp;
}
