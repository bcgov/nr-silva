package ca.bc.gov.restapi.results.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | CodeConverterUtil")
class CodeConverterUtilTest {

  @Test
  @DisplayName("Convert single entity to DTO should succeed")
  void toCodeDescriptionDtos_singleEntity_shouldSucceed() {
    // Given
    GenericCodeEntity entity = createTestEntity("TEST", "Test Description");
    List<? extends GenericCodeEntity> entities = List.of(entity);

    // When
    List<CodeDescriptionDto> result = CodeConverterUtil.toCodeDescriptionDtos(entities);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).code()).isEqualTo("TEST");
    assertThat(result.get(0).description()).isEqualTo("Test Description");
  }

  @Test
  @DisplayName("Convert multiple entities to DTOs should succeed")
  void toCodeDescriptionDtos_multipleEntities_shouldSucceed() {
    // Given
    List<? extends GenericCodeEntity> entities =
        List.of(
            createTestEntity("CODE1", "Description 1"),
            createTestEntity("CODE2", "Description 2"),
            createTestEntity("CODE3", "Description 3"));

    // When
    List<CodeDescriptionDto> result = CodeConverterUtil.toCodeDescriptionDtos(entities);

    // Then
    assertThat(result).hasSize(3);
    assertThat(result)
        .extracting(CodeDescriptionDto::code)
        .containsExactly("CODE1", "CODE2", "CODE3");
    assertThat(result)
        .extracting(CodeDescriptionDto::description)
        .containsExactly("Description 1", "Description 2", "Description 3");
  }

  @Test
  @DisplayName("Convert empty list should return empty list")
  void toCodeDescriptionDtos_emptyList_shouldReturnEmpty() {
    // Given
    List<GenericCodeEntity> entities = new ArrayList<>();

    // When
    List<CodeDescriptionDto> result = CodeConverterUtil.toCodeDescriptionDtos(entities);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Convert entities with null descriptions should preserve null")
  void toCodeDescriptionDtos_nullDescription_shouldPreserveNull() {
    // Given
    GenericCodeEntity entity = createTestEntity("CODE", null);
    List<? extends GenericCodeEntity> entities = List.of(entity);

    // When
    List<CodeDescriptionDto> result = CodeConverterUtil.toCodeDescriptionDtos(entities);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).code()).isEqualTo("CODE");
    assertThat(result.get(0).description()).isNull();
  }

  @Test
  @DisplayName("Convert entities with special characters should succeed")
  void toCodeDescriptionDtos_specialCharacters_shouldSucceed() {
    // Given
    GenericCodeEntity entity = createTestEntity("ABC", "Description with & special <chars>");
    List<? extends GenericCodeEntity> entities = List.of(entity);

    // When
    List<CodeDescriptionDto> result = CodeConverterUtil.toCodeDescriptionDtos(entities);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).description()).isEqualTo("Description with & special <chars>");
  }

  @Test
  @DisplayName("Convert maintains entity order")
  void toCodeDescriptionDtos_maintainOrder_shouldSucceed() {
    // Given
    List<? extends GenericCodeEntity> entities =
        List.of(
            createTestEntity("ZZZ", "Last"),
            createTestEntity("AAA", "First"),
            createTestEntity("MMM", "Middle"));

    // When
    List<CodeDescriptionDto> result = CodeConverterUtil.toCodeDescriptionDtos(entities);

    // Then
    assertThat(result).extracting(CodeDescriptionDto::code).containsExactly("ZZZ", "AAA", "MMM");
  }

  private GenericCodeEntity createTestEntity(String code, String description) {
    return TestGenericCodeEntity.builder()
        .code(code)
        .description(description)
        .effectiveDate(LocalDate.now())
        .expiryDate(LocalDate.now().plusYears(1))
        .updateTimestamp(LocalDateTime.now())
        .build();
  }

  /** Concrete implementation of GenericCodeEntity for testing purposes. */
  @SuperBuilder
  static class TestGenericCodeEntity extends GenericCodeEntity {}
}
