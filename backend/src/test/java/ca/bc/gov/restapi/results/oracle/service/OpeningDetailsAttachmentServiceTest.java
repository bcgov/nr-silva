package ca.bc.gov.restapi.results.oracle.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsAttachmentMetaDto;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentMetaProjection;
import ca.bc.gov.restapi.results.oracle.repository.OpeningAttachmentRepository;
import ca.bc.gov.restapi.results.oracle.service.opening.details.OpeningDetailsAttachmentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Opening Details Attachment Service")
class OpeningDetailsAttachmentServiceTest {

  @Mock OpeningAttachmentRepository openingAttachmentRepository;

  private OpeningDetailsAttachmentService service;

  @BeforeEach
  void setUp() {
    service = new OpeningDetailsAttachmentService(openingAttachmentRepository);
  }

  @Test
  @DisplayName("getAttachmentList should return populated DTOs from projections")
  void getAttachmentList_shouldReturnMetadataList() {
    Long openingId = 100L;

    OpeningAttachmentMetaProjection projection = mock(OpeningAttachmentMetaProjection.class);
    when(projection.getOpeningId()).thenReturn(openingId);
    when(projection.getAttachmentName()).thenReturn("file.pdf");
    when(projection.getAttachmentDescription()).thenReturn("Test File");
    when(projection.getMimeTypeCode()).thenReturn("PDF");
    when(projection.getEntryUserId()).thenReturn("userA");
    when(projection.getEntryTimestamp()).thenReturn(LocalDateTime.now().minusDays(1));
    when(projection.getUpdateUserId()).thenReturn("userB");
    when(projection.getUpdateTimestamp()).thenReturn(LocalDateTime.now());
    when(projection.getRevisionCount()).thenReturn(2);
    when(projection.getAttachmentGuid()).thenReturn("f47ac10b-58cc-4372-a567-0e02b2c3d479");
    when(projection.getAttachmentSize()).thenReturn(4096L);

    when(openingAttachmentRepository.findByOpeningId(openingId)).thenReturn(List.of(projection));

    List<OpeningDetailsAttachmentMetaDto> result = service.getAttachmentList(openingId);

    assertEquals(1, result.size());
    OpeningDetailsAttachmentMetaDto dto = result.get(0);
    assertEquals(openingId, dto.openingId());
    assertEquals("file.pdf", dto.attachmentName());
    assertEquals("Test File", dto.attachmentDescription());
    assertEquals("PDF", dto.mimeTypeCode());
    assertEquals("f47ac10b-58cc-4372-a567-0e02b2c3d479", dto.attachmentGuid());
    assertEquals(4096L, dto.attachmentSize());
  }

  @Test
  @DisplayName("getAttachmentList should return empty list if no records found")
  void getAttachmentList_shouldReturnEmptyList() {
    Long openingId = 999L;
    when(openingAttachmentRepository.findByOpeningId(openingId)).thenReturn(List.of());

    List<OpeningDetailsAttachmentMetaDto> result = service.getAttachmentList(openingId);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("getAttachmentEntity should return entity when found")
  void getAttachmentEntity_shouldReturnOptionalEntity() {
    UUID guid = UUID.randomUUID();
    OpeningAttachmentEntity entity = mock(OpeningAttachmentEntity.class);
    when(openingAttachmentRepository.findByAttachmentGuid(guid)).thenReturn(Optional.of(entity));

    Optional<OpeningAttachmentEntity> result = service.getAttachmentEntity(guid);

    assertTrue(result.isPresent());
    assertEquals(entity, result.get());
  }

  @Test
  @DisplayName("getAttachmentEntity should return empty if not found")
  void getAttachmentEntity_shouldReturnEmpty() {
    UUID guid = UUID.randomUUID();
    when(openingAttachmentRepository.findByAttachmentGuid(guid)).thenReturn(Optional.empty());

    Optional<OpeningAttachmentEntity> result = service.getAttachmentEntity(guid);

    assertTrue(result.isEmpty());
  }
}
