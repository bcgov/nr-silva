package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * This class represents a CUT_BLOCK_OPEN_ADMIN entity in the database.
 */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "THE", name = "CUT_BLOCK_OPEN_ADMIN")
@Entity
public class CutBlockOpenAdminEntity {

  @Id
  @Column(name = "CUT_BLOCK_OPEN_ADMIN_ID")
  private Long id;

  @Column(name = "FOREST_FILE_ID", length = 10)
  private String forestFileId;

  @Column(name = "CUTTING_PERMIT_ID", length = 3)
  private String cuttingPermitId;

  @Column(name = "TIMBER_MARK", length = 6)
  private String timberMark;

  @Column(name = "CUT_BLOCK_ID", length = 10)
  private String cutBlockId;

  @Column(name = "OPENING_GROSS_AREA", precision = 11, scale = 4)
  private BigDecimal openingGrossArea;

  @Column(name = "DISTURBANCE_START_DATE")
  private LocalDate disturbanceStartDate;

  @Column(name = "DISTURBANCE_END_DATE")
  private LocalDate disturbanceEndDate;

  @Column(name = "OPENING_ID")
  private Long openingId;
}
