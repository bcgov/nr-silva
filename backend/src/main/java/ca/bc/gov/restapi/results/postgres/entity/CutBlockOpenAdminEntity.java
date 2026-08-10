package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "cut_block_open_admin")
public class CutBlockOpenAdminEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cboa_seq")
  @SequenceGenerator(
      name = "cboa_seq",
      sequenceName = "silva.cut_block_open_admin_id_seq",
      allocationSize = 1)
  @Column(name = "cut_block_open_admin_id")
  private Long id;

  @Column(name = "forest_file_id", length = 10)
  private String forestFileId;

  @Column(name = "cutting_permit_id", length = 3)
  private String cuttingPermitId;

  @Column(name = "timber_mark", length = 10)
  private String timberMark;

  @Column(name = "cut_block_id", length = 10)
  private String cutBlockId;

  @Column(name = "opening_gross_area", precision = 11, scale = 4)
  private BigDecimal openingGrossArea;

  @Column(name = "disturbance_start_date")
  private LocalDate disturbanceStartDate;

  @Column(name = "disturbance_end_date")
  private LocalDate disturbanceEndDate;

  @Column(name = "opening_id")
  private Long openingId;

  @Column(name = "cb_skey")
  private Long cbSkey;

  @Column(name = "opening_prime_licence_ind", length = 1)
  private String openingPrimeLicenceInd;

  @Column(name = "revision_count")
  private Integer revisionCount;

  @Column(name = "entry_userid", length = 30)
  private String entryUserId;

  @Column(name = "entry_timestamp")
  private LocalDateTime entryTimestamp;

  @Column(name = "update_userid", length = 30)
  private String updateUserId;

  @Column(name = "update_timestamp")
  private LocalDateTime updateTimestamp;
}
