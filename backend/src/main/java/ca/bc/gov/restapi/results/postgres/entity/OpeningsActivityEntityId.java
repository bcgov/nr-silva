package ca.bc.gov.restapi.results.postgres.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Composite key for {@link OpeningsActivityEntity}. */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OpeningsActivityEntityId {

  @NonNull private Integer activityId;

  @NonNull private Long openingId;
}
