package ca.bc.gov.restapi.results.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | StringUtil")
class StringUtilTest {

  @Test
  @DisplayName("Convert single string to uppercase should succeed")
  void toUpperCase_singleString_shouldSucceed() {
    // Given
    List<String> values = List.of("test");

    // When
    List<String> result = StringUtil.toUpperCase(values);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo("TEST");
  }

  @Test
  @DisplayName("Convert multiple strings to uppercase should succeed")
  void toUpperCase_multipleStrings_shouldSucceed() {
    // Given
    List<String> values = List.of("abc", "def", "ghi");

    // When
    List<String> result = StringUtil.toUpperCase(values);

    // Then
    assertThat(result).hasSize(3);
    assertThat(result).containsExactly("ABC", "DEF", "GHI");
  }

  @Test
  @DisplayName("Convert mixed case strings to uppercase should succeed")
  void toUpperCase_mixedCaseStrings_shouldSucceed() {
    // Given
    List<String> values = List.of("TeSt", "CaSe", "MiXeD");

    // When
    List<String> result = StringUtil.toUpperCase(values);

    // Then
    assertThat(result).hasSize(3);
    assertThat(result).containsExactly("TEST", "CASE", "MIXED");
  }

  @Test
  @DisplayName("Convert already uppercase strings should return same values")
  void toUpperCase_uppercaseStrings_shouldReturnSameValues() {
    // Given
    List<String> values = List.of("ALREADY", "UPPERCASE", "VALUES");

    // When
    List<String> result = StringUtil.toUpperCase(values);

    // Then
    assertThat(result).hasSize(3);
    assertThat(result).containsExactly("ALREADY", "UPPERCASE", "VALUES");
  }

  @Test
  @DisplayName("Convert empty list should return empty list")
  void toUpperCase_emptyList_shouldReturnEmptyList() {
    // Given
    List<String> values = new ArrayList<>();

    // When
    List<String> result = StringUtil.toUpperCase(values);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Convert null list should return null")
  void toUpperCase_nullList_shouldReturnNull() {
    // Given
    List<String> values = null;

    // When
    List<String> result = StringUtil.toUpperCase(values);

    // Then
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Convert list with null elements should handle null values")
  void toUpperCase_listWithNullElements_shouldHandleNullValues() {
    // Given
    List<String> values = new ArrayList<>();
    values.add("test");
    values.add(null);
    values.add("value");

    // When/Then - This should throw NullPointerException as expected behavior
    // when trying to uppercase null
    try {
      StringUtil.toUpperCase(values);
      assertThat(false).as("Should have thrown NullPointerException").isTrue();
    } catch (NullPointerException e) {
      assertThat(e).isNotNull();
    }
  }

  @Test
  @DisplayName("Convert strings with special characters should preserve them")
  void toUpperCase_specialCharacters_shouldPreserveThem() {
    // Given
    List<String> values = List.of("test-123", "value_456", "code@789");

    // When
    List<String> result = StringUtil.toUpperCase(values);

    // Then
    assertThat(result).hasSize(3);
    assertThat(result).containsExactly("TEST-123", "VALUE_456", "CODE@789");
  }

  @Test
  @DisplayName("Convert strings with spaces should preserve them")
  void toUpperCase_withSpaces_shouldPreserveThem() {
    // Given
    List<String> values = List.of("hello world", "test value", "code base");

    // When
    List<String> result = StringUtil.toUpperCase(values);

    // Then
    assertThat(result).hasSize(3);
    assertThat(result).containsExactly("HELLO WORLD", "TEST VALUE", "CODE BASE");
  }

  @Test
  @DisplayName("Convert empty strings should return empty strings")
  void toUpperCase_emptyStrings_shouldReturnEmptyStrings() {
    // Given
    List<String> values = List.of("", " ", "test");

    // When
    List<String> result = StringUtil.toUpperCase(values);

    // Then
    assertThat(result).hasSize(3);
    assertThat(result).containsExactly("", " ", "TEST");
  }
}
