package ca.bc.gov.restapi.results.oracle.entity.code;

import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base mapped superclass for simple code/description lookup tables. Subclasses should supply the
 * code/@Id column with the appropriate column name.
 */
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AttributeOverrides({
  @AttributeOverride(
      name = "description",
      column = @Column(name = "DESCRIPTION", length = 120, nullable = false)),
  @AttributeOverride(
      name = "effectiveDate",
      column = @Column(name = "EFFECTIVE_DATE", nullable = false)),
  @AttributeOverride(name = "expiryDate", column = @Column(name = "EXPIRY_DATE", nullable = false)),
  @AttributeOverride(
      name = "updateTimestamp",
      column = @Column(name = "UPDATE_TIMESTAMP", nullable = false))
})
public abstract class AbstractCodeOracleEntity extends GenericCodeEntity {}
