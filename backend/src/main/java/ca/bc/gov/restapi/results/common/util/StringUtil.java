package ca.bc.gov.restapi.results.common.util;

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
   * @return a new list with all strings converted to uppercase, or null if input is null
   */
  public static List<String> toUpperCase(List<String> values) {
    if (values == null) {
      return null;
    }
    return values.stream().map(String::toUpperCase).toList();
  }
}
