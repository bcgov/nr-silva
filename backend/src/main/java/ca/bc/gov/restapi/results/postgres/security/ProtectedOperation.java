package ca.bc.gov.restapi.results.postgres.security;

/**
 * Operations protected by role-based access control. Each one must have its allowed roles
 * registered in {@link OperationAccessPolicy}.
 */
public enum ProtectedOperation {
  /**
   * Creating an opening. The shared tenure validation of opening creation, tenure validation and
   * tenure editing also uses this rule.
   */
  CREATE_OPENING,
  /** Creating an activity under an opening. */
  CREATE_ACTIVITY
}
