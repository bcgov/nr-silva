package ca.bc.gov.restapi.results.common.util;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class UriUtils {

  /**
   * Builds a MultiValueMap with the given key mapped to all values in the list.
   *
   * @param key the query parameter key
   * @param values the list of values to associate with the key
   * @return a MultiValueMap suitable for use with UriComponentsBuilder.queryParams()
   */
  public static MultiValueMap<String, String> buildMultiValueQueryParam(
      String key, List<String> values) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    if (values != null) {
      values.stream().filter(StringUtils::isNotBlank).forEach(value -> map.add(key, value));
    }
    return map;
  }
}
