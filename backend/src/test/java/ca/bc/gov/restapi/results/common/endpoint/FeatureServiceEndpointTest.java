package ca.bc.gov.restapi.results.common.endpoint;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.service.OpenMapsService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FeatureServiceEndpoint.class)
@WithMockUser(roles = "user_read")
class FeatureServiceEndpointTest {

  @Autowired private MockMvc mockMvc;

  @MockBean
  OpenMapsService openMapsService;

  @Test
  @DisplayName("Get opening polygon and properties happy path should succeed")
  void getOpeningPolygonAndProperties_happyPath_shouldSucceed() throws Exception {
    String openingId = "58993";

    String response =
        """
        {
          "type": "FeatureCollection",
          "features": [
            {
              "type": "Feature",
              "id": "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW.fid-6f119ee7_19129391292_7c7b",
              "geometry_name": "GEOMETRY",
              "properties": {
                "OPENING_ID": 58993,
                "OPENING_CATEGORY_CODE": "FTML",
                "OPENING_STATUS_CODE": "FG",
                "REGION_CODE": "ROM",
                "REGION_NAME": "Omineca Natural Resource Region",
                "DISTRICT_CODE": "DMK",
                "DISTRICT_NAME": "Mackenzie Natural Resource District",
                "CLIENT_NAME": "CONIFEX MACKENZIE FOREST PRODUCTS INC.",
                "CLIENT_NUMBER": "00161229",
                "OPENING_WHO_CREATED": "MLSIS",
                "OPENING_WHEN_CREATED": "1999-08-26Z",
                "OPENING_WHO_UPDATED": "IDIR\\\\\\\\SCAKERLE",
                "OPENING_WHEN_UPDATED": "2023-11-14Z",
                "OBJECTID": 3869732,
                "bbox": [
                  -125.6056,
                  56.06894,
                  -125.59267,
                  56.07429
                ]
              }
            }
          ]
        }
        """;

    when(openMapsService.getOpeningPolygonAndProperties(openingId))
        .thenReturn(ResponseEntity.of(Optional.of(response)));

    mockMvc
        .perform(
            get("/api/feature-service/polygon-and-props/{openingId}", openingId)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.type").value("FeatureCollection"))
        .andExpect(jsonPath("$.features[0].type").value("Feature"))
        .andExpect(
            jsonPath("$.features[0].id")
                .value("WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW.fid-6f119ee7_19129391292_7c7b"))
        .andExpect(jsonPath("$.features[0].geometry_name").value("GEOMETRY"))
        .andExpect(jsonPath("$.features[0].properties.OPENING_ID").value(openingId))
        .andExpect(jsonPath("$.features[0].properties.OPENING_CATEGORY_CODE").value("FTML"))
        .andExpect(jsonPath("$.features[0].properties.OPENING_STATUS_CODE").value("FG"))
        .andExpect(jsonPath("$.features[0].properties.REGION_CODE").value("ROM"))
        .andExpect(
            jsonPath("$.features[0].properties.REGION_NAME")
                .value("Omineca Natural Resource Region"))
        .andExpect(jsonPath("$.features[0].properties.DISTRICT_CODE").value("DMK"))
        .andExpect(
            jsonPath("$.features[0].properties.DISTRICT_NAME")
                .value("Mackenzie Natural Resource District"))
        .andExpect(
            jsonPath("$.features[0].properties.CLIENT_NAME")
                .value("CONIFEX MACKENZIE FOREST PRODUCTS INC."))
        .andExpect(jsonPath("$.features[0].properties.CLIENT_NUMBER").value("00161229"))
        .andExpect(jsonPath("$.features[0].properties.OPENING_WHO_CREATED").value("MLSIS"))
        .andExpect(jsonPath("$.features[0].properties.OPENING_WHEN_CREATED").value("1999-08-26Z"))
        .andExpect(
            jsonPath("$.features[0].properties.OPENING_WHO_UPDATED").value("IDIR\\\\SCAKERLE"))
        .andExpect(jsonPath("$.features[0].properties.OPENING_WHEN_UPDATED").value("2023-11-14Z"))
        .andExpect(jsonPath("$.features[0].properties.OBJECTID").value("3869732"))
        .andExpect(jsonPath("$.features[0].properties.bbox[0]").value("-125.6056"))
        .andExpect(jsonPath("$.features[0].properties.bbox[1]").value("56.06894"))
        .andExpect(jsonPath("$.features[0].properties.bbox[2]").value("-125.59267"))
        .andExpect(jsonPath("$.features[0].properties.bbox[3]").value("56.07429"))
        .andReturn();
  }
}
