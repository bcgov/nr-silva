package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "cut_block")
public class CutBlockEntity {

  @Id
  @Column(name = "cb_skey")
  private Long cbSkey;

  @Column(name = "forest_file_id", length = 10, nullable = false)
  private String forestFileId;

  @Column(name = "cutting_permit_id", length = 3)
  private String cuttingPermitId;

  @Column(name = "timber_mark", length = 10)
  private String timberMark;

  @Column(name = "cut_block_id", length = 10, nullable = false)
  private String cutBlockId;

  @Column(name = "block_status_st", length = 3, nullable = false)
  private String blockStatusSt;

  @Column(name = "sp_exempt_ind", length = 1, nullable = false)
  private String spExemptInd;

  @Column(name = "revision_count", nullable = false)
  private Integer revisionCount;

  @Column(name = "entry_userid", length = 30, nullable = false)
  private String entryUserId;

  @Column(name = "entry_timestamp", nullable = false)
  private LocalDateTime entryTimestamp;

  @Column(name = "update_userid", length = 30, nullable = false)
  private String updateUserId;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;
}
