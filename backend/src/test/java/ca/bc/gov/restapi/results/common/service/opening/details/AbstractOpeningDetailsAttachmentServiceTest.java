package ca.bc.gov.restapi.results.common.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsAttachmentMetaDto;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningAttachmentMetaProjection;
import ca.bc.gov.restapi.results.common.repository.OpeningAttachmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Opening Details Attachment Service | Contract")
public abstract class AbstractOpeningDetailsAttachmentServiceTest<T extends OpeningDetailsAttachmentService> {

  @Mock
  protected OpeningAttachmentRepository openingAttachmentRepository;

  protected OpeningDetailsAttachmentService service;

  protected abstract T createService(OpeningAttachmentRepository openingAttachmentRepository);

  @BeforeEach
  void setup() {
    service = createService(openingAttachmentRepository);
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
    when(projection.getAttachmentGuid()).thenReturn("F47AC10B58CC4372A5670E02B2C3D479");

    when(openingAttachmentRepository.findByOpeningId(openingId)).thenReturn(List.of(projection));

    List<OpeningDetailsAttachmentMetaDto> result = service.getAttachmentList(openingId);

    assertEquals(1, result.size());
    OpeningDetailsAttachmentMetaDto dto = result.get(0);
    assertEquals(openingId, dto.openingId());
    assertEquals("file.pdf", dto.attachmentName());
    assertEquals("Test File", dto.attachmentDescription());
    assertEquals("PDF", dto.mimeTypeCode());
    assertEquals("F47AC10B58CC4372A5670E02B2C3D479", dto.attachmentGuid());
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
}
