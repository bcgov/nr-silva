package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsAttachmentMetaDto;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentMetaProjection;
import ca.bc.gov.restapi.results.oracle.repository.OpeningAttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningDetailsAttachmentService {
    private final OpeningAttachmentRepository openingAttachmentRepository;

    /**
     * Retrieves a list of attachment metadata records for the specified opening ID.
     *
     * @param openingId the ID of the opening
     * @return list of attachment metadata records
     */
    public List<OpeningDetailsAttachmentMetaDto> getAttachmentList(Long openingId) {
        List<OpeningAttachmentMetaProjection> projections = openingAttachmentRepository.findByOpeningId(openingId);

        return projections.stream()
                .map(p -> new OpeningDetailsAttachmentMetaDto(
                        p.getOpeningId(),
                        p.getAttachmentName(),
                        p.getAttachmentDescription(),
                        p.getMimeTypeCode(),
                        p.getEntryUserId(),
                        p.getEntryTimestamp(),
                        p.getUpdateUserId(),
                        p.getUpdateTimestamp(),
                        p.getRevisionCount(),
                        p.getAttachmentGuid(),
                        p.getAttachmentSize()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the binary content of an attachment by its GUID.
     *
     * @param guid The unique identifier of the attachment.
     * @return The attachment's file data as a byte array.
     * @throws OpenApiResourceNotFoundException if no attachment is found for the given GUID.
     */
    public Optional<OpeningAttachmentEntity> getAttachmentEntity(UUID guid) {
        return openingAttachmentRepository.findByAttachmentGuid(guid);
    }
}
