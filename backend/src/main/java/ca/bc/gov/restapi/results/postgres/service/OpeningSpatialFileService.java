package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.SilvaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/** This service handles processing of uploaded spatial files (GeoJSON, GML, ESF/XML). */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningSpatialFileService {
  /**
   * Process an uploaded spatial file. Depending on the file extension, delegates to the appropriate
   * handler.
   *
   * @param file the uploaded spatial file
   */
  public void processOpeningSpatialFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is null or empty");
    }

    if (file.getSize() > SilvaConstants.MAX_OPENING_FILE_SIZE_BYTES) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File exceeds 25MB size limit");
    }

    String fileName = file.getOriginalFilename();
    if (fileName == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file has no name");
    }

    String lowerName = fileName.toLowerCase();

    if (lowerName.endsWith(".xml")) {
      processEsf(file);
    } else if (lowerName.endsWith(".gml")) {
      processGml(file);
    } else if (lowerName.endsWith(".geojson") || lowerName.endsWith(".json")) {
      processGeojson(file);
    } else {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Unsupported file type: " + fileName);
    }
  }

  private void processEsf(MultipartFile file) {
    // TODO implement ESF processing
    log.info("Processing ESF/XML file: {}", file.getOriginalFilename());
  }

  private void processGml(MultipartFile file) {
    // TODO implement GML processing
    log.info("Processing GML file: {}", file.getOriginalFilename());
  }

  private void processGeojson(MultipartFile file) {
    // TODO implement GeoJSON processing
    log.info("Processing GeoJSON file: {}", file.getOriginalFilename());
  }
}
