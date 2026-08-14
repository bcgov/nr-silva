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
@Table(schema = "silva", name = "cut_block_client")
public class CutBlockClientEntity {

  @Id
  @Column(name = "cblk_client_skey")
  private Long cblkClientSkey;

  @Column(name = "cb_skey", nullable = false)
  private Long cbSkey;

  @Column(name = "cut_block_client_type_code", length = 1, nullable = false)
  private String cutBlockClientTypeCode;

  @Column(name = "licensee_start_date")
  private LocalDateTime licenseeStartDate;

  @Column(name = "licensee_end_date")
  private LocalDateTime licenseeEndDate;

  @Column(name = "client_locn_code", length = 2, nullable = false)
  private String clientLocnCode;

  @Column(name = "client_number", length = 8, nullable = false)
  private String clientNumber;

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
