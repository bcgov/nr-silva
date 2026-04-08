package ca.bc.gov.restapi.results.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.common.SilvaConstants;
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
  @DisplayName("Convert null list should throw NullPointerException")
  void toUpperCase_nullList_shouldThrowNullPointerException() {
    // Given
    List<String> values = null;

    // When/Then - Should throw NullPointerException
    try {
      StringUtil.toUpperCase(values);
      assertThat(false).as("Should have thrown NullPointerException").isTrue();
    } catch (NullPointerException e) {
      assertThat(e).isNotNull();
    }
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

  @Test
  @DisplayName("nullIfBlank | null value | should return null")
  void nullIfBlank_nullValue_shouldReturnNull() {
    assertThat(StringUtil.nullIfBlank(null)).isNull();
  }

  @Test
  @DisplayName("nullIfBlank | empty string | should return null")
  void nullIfBlank_emptyString_shouldReturnNull() {
    assertThat(StringUtil.nullIfBlank("")).isNull();
  }

  @Test
  @DisplayName("nullIfBlank | whitespace only | should return null")
  void nullIfBlank_whitespaceOnly_shouldReturnNull() {
    assertThat(StringUtil.nullIfBlank("   ")).isNull();
  }

  @Test
  @DisplayName("nullIfBlank | single space | should return null")
  void nullIfBlank_singleSpace_shouldReturnNull() {
    assertThat(StringUtil.nullIfBlank(" ")).isNull();
  }

  @Test
  @DisplayName("nullIfBlank | non-blank value | should return original value")
  void nullIfBlank_nonBlankValue_shouldReturnOriginalValue() {
    assertThat(StringUtil.nullIfBlank("hello")).isEqualTo("hello");
  }

  @Test
  @DisplayName("nullIfBlank | value with surrounding spaces | should return original value")
  void nullIfBlank_valueWithSurroundingSpaces_shouldReturnOriginalValue() {
    assertThat(StringUtil.nullIfBlank("  hello  ")).isEqualTo("  hello  ");
  }

  @Test
  @DisplayName("isFilterSet | exactly [NOVALUE] | should return false")
  void isFilterSet_singleNovalue_shouldReturnFalse() {
    assertThat(StringUtil.isFilterSet(List.of(SilvaConstants.NOVALUE))).isFalse();
  }

  @Test
  @DisplayName("isFilterSet | single real value | should return true")
  void isFilterSet_singleRealValue_shouldReturnTrue() {
    assertThat(StringUtil.isFilterSet(List.of("APP"))).isTrue();
  }

  @Test
  @DisplayName("isFilterSet | multiple real values | should return true")
  void isFilterSet_multipleRealValues_shouldReturnTrue() {
    assertThat(StringUtil.isFilterSet(List.of("APP", "AMD"))).isTrue();
  }

  @Test
  @DisplayName("isFilterSet | NOVALUE plus real value | should return true")
  void isFilterSet_novaleAndRealValue_shouldReturnTrue() {
    // A caller that supplies NOVALUE alongside a real value must not be blocked
    assertThat(StringUtil.isFilterSet(List.of(SilvaConstants.NOVALUE, "APP"))).isTrue();
  }

  @Test
  @DisplayName("isFilterSet | real value plus NOVALUE | should return true")
  void isFilterSet_realValueAndNovalue_shouldReturnTrue() {
    assertThat(StringUtil.isFilterSet(List.of("APP", SilvaConstants.NOVALUE))).isTrue();
  }

  @Test
  @DisplayName("isFilterSet | multiple NOVALUE entries | should return true")
  void isFilterSet_multipleNovalues_shouldReturnTrue() {
    // Two NOVALUEs means the caller explicitly sent the string twice — treat as set
    assertThat(StringUtil.isFilterSet(List.of(SilvaConstants.NOVALUE, SilvaConstants.NOVALUE)))
        .isTrue();
  }
}
