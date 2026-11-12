package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Read-only view of silva.forest_file_client: the clients of a forest file by type. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "forest_file_client")
public class ForestFileClientEntity {

  @Id
  @Column(name = "forest_file_client_skey")
  private Long forestFileClientSkey;

  @Column(name = "forest_file_id", length = 10, nullable = false)
  private String forestFileId;

  @Column(name = "client_number", length = 8, nullable = false)
  private String clientNumber;

  @Column(name = "forest_file_client_type_code", length = 1, nullable = false)
  private String forestFileClientTypeCode;
}
