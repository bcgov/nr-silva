package ca.bc.gov.restapi.results.common.util;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Utility for string operations. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

  /**
   * Converts all strings in the list to uppercase for case-insensitive matching.
   *
   * @param values the list of strings to convert
   * @return a new list with all strings converted to uppercase
   */
  public static List<String> toUpperCase(List<String> values) {
    return values.stream().map(String::toUpperCase).toList();
  }

  /**
   * Returns {@code true} when the list contains real filter values — i.e., it is not the sentinel
   * singleton {@code [NOVALUE]} substituted when no values were supplied by the caller.
   *
   * <p>The check is intentionally stricter than {@code !NOVALUE.equals(list.get(0))}: a list with
   * more than one element is always considered set, even if the first element happens to be the
   * sentinel string.
   *
   * @param list the filter list to inspect; must not be {@code null}
   * @return {@code true} if the list holds at least one real filter value
   */
  public static boolean isFilterSet(List<String> list) {
    return list.size() != 1 || !SilvaConstants.NOVALUE.equals(list.get(0));
  }
}
