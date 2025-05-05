package ca.bc.gov.restapi.results.oracle.entity.activities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(schema = "THE", name = "ACTIVITY_TREATMENT_UNIT")
public class ActivityTreatmentUnitEntity {

  @Column(name = "ACTIVITY_TREATMENT_UNIT_ID")
  @Id
  private Long id;

}
