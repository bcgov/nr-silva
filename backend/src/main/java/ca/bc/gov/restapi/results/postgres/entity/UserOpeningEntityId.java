package ca.bc.gov.restapi.results.postgres.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Composite key for {@link UserOpeningEntity}. */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserOpeningEntityId {

  @NonNull private String userId;
  @NonNull private String openingId;
}
