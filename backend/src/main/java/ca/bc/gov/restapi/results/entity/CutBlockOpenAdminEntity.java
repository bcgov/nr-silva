package ca.bc.gov.restapi.results.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/** This class represents a CUT_BLOCK_OPEN_ADMIN entity in the database. */
@Getter
@Setter
@Table(name = "CUT_BLOCK_OPEN_ADMIN")
@Entity
public class CutBlockOpenAdminEntity {

  @Id
  @Column(name = "CUT_BLOCK_OPEN_ADMIN_ID")
  private Long id;

  @Column(name = "FOREST_FILE_ID", length = 10, nullable = true)
  private String forestFileId;

  @Column(name = "CUTTING_PERMIT_ID", length = 3, nullable = true)
  private String cuttingPermitId;

  @Column(name = "TIMBER_MARK", length = 6, nullable = true)
  private String timberMark;

  @Column(name = "CUT_BLOCK_ID", length = 10, nullable = true)
  private String cutBlockId;

  @Column(name = "OPENING_GROSS_AREA", nullable = true, precision = 4)
  private BigDecimal openingGrossArea;

  @Column(name = "DISTURBANCE_START_DATE", nullable = true)
  private LocalDate disturbanceStartDate;

  @Column(name = "DISTURBANCE_END_DATE", nullable = true)
  private LocalDate disturbanceEndDate;

  @Column(name = "OPENING_ID", length = 10)
  private Long openingId;
}
