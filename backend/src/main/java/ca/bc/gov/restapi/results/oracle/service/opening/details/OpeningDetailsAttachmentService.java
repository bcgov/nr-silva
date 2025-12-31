package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsAttachmentMetaDto;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningAttachmentMetaProjection;
import ca.bc.gov.restapi.results.oracle.repository.OpeningAttachmentRepository;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningDetailsAttachmentService {

  private final OpeningAttachmentRepository openingAttachmentRepository;

  @Value("${ca.bc.gov.nrs.s3-bucket}")
  String BUCKET;

  @Value("${ca.bc.gov.nrs.s3-access-key}")
  String ACCESS_KEY = "your-access-key";

  @Value("${ca.bc.gov.nrs.s3-secret-key}")
  String SECRET_KEY = "your-secret-key";

  @Value("${ca.bc.gov.nrs.s3-endpoint}")
  String ENDPOINT;

  /**
   * Retrieves a list of attachment metadata records for the specified opening ID.
   *
   * @param openingId the ID of the opening
   * @return list of attachment metadata records
   */
  public List<OpeningDetailsAttachmentMetaDto> getAttachmentList(Long openingId) {
    List<OpeningAttachmentMetaProjection> projections =
        openingAttachmentRepository.findByOpeningId(openingId);

    return projections.stream()
        .map(
            p ->
                new OpeningDetailsAttachmentMetaDto(
                    p.getOpeningId(),
                    p.getAttachmentName(),
                    p.getAttachmentDescription(),
                    p.getMimeTypeCode(),
                    p.getEntryUserId(),
                    p.getEntryTimestamp(),
                    p.getUpdateUserId(),
                    p.getUpdateTimestamp(),
                    p.getRevisionCount(),
                    p.getAttachmentGuid()))
        .toList();
  }

  /**
   * Generates a pre-signed URL to download an attachment from Object Store using the GUID as key.
   *
   * @param guid the attachment GUID
   * @return pre-signed download URL
   */
  public String getS3PresignedUrl(String guid) {
    try (S3Presigner presigner =
        S3Presigner.builder()
            .endpointOverride(URI.create(ENDPOINT))
            .region(Region.US_EAST_1) // Region is required for the sdk, using a dummy region
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .build()) {

      // S3 object keys are case-sensitive
      String upperCaseGuid = guid.toUpperCase();

      // Query the DB for file name
      OpeningAttachmentMetaProjection attachmentProj =
          openingAttachmentRepository
              .findByAttachmentGuid(guid)
              .orElseThrow(() -> new NotFoundGenericException("Attachemnt"));

      GetObjectRequest getObjectRequest =
          GetObjectRequest.builder()
              .bucket(BUCKET)
              .key(upperCaseGuid)
              .responseContentDisposition(
                  "attachment; filename=\"" + attachmentProj.getAttachmentName() + "\"")
              .build();

      GetObjectPresignRequest presignRequest =
          GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofMinutes(5))
              .getObjectRequest(getObjectRequest)
              .build();

      PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
      return presignedRequest.url().toString();
    }
  }
}
