package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.service.OpeningSpatialFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Integration Test | Opening Upload Endpoint")
@AutoConfigureMockMvc
@WithMockJwt
class OpeningEndpointUploadIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private OpeningSpatialFileService openingSpatialFileService;

  @Autowired private ObjectMapper mapper;

  @Test
  @DisplayName("Should accept upload and return accepted")
  void shouldAcceptUpload() throws Exception {
    ExtractedGeoDataDto dto =
        ExtractedGeoDataDto.builder()
            .metaData(null)
            .geoJson(mapper.createObjectNode())
            .tenureList(null)
            .build();
    when(openingSpatialFileService.processOpeningSpatialFile(any())).thenReturn(dto);

    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "test.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            "{\"type\":\"FeatureCollection\",\"features\":[]}".getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings/create/upload")
                .file(file)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted());
  }
}

@TestConfiguration
class OpeningEndpointUploadIntegrationTestConfig {

  @Bean
  public OpeningSpatialFileService openingSpatialFileService() {
    return Mockito.mock(OpeningSpatialFileService.class);
  }
}
