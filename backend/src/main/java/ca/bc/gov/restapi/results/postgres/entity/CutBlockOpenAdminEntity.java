package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "cut_block_open_admin")
public class CutBlockOpenAdminEntity {
  @Id
  @Column(name = "cut_block_open_admin_id")
  private Long id;

  @Column(name = "forest_file_id", length = 10)
  private String forestFileId;

  @Column(name = "cutting_permit_id", length = 3)
  private String cuttingPermitId;

  @Column(name = "timber_mark", length = 6)
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
}
