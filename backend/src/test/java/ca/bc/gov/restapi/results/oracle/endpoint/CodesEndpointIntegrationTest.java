package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WithMockJwt
@DisplayName("Integrated Test | Opening Search Endpoint")
class CodesEndpointIntegrationTest  extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;


  @Test
  @DisplayName("Get Opening Categories happy Path should Succeed")
  void getOpeningCategories_happyPath_shouldSucceed() throws Exception {
    CodeDescriptionDto category = new CodeDescriptionDto("BLCF",
        "Backlog SP Area - Community Forest (Expired)");

    mockMvc
        .perform(
            get("/api/codes/categories")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].code").value(category.code()))
        .andExpect(jsonPath("$[0].description").value(category.description()))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units happy Path should Succeed")
  void getOpeningOrgUnits_happyPath_shouldSucceed() throws Exception {
    OrgUnitEntity orgUnit = new OrgUnitEntity();
    orgUnit.setOrgUnitNo(1L);
    orgUnit.setOrgUnitCode("DAS");
    orgUnit.setOrgUnitName("Org one");
    orgUnit.setLocationCode("123");
    orgUnit.setAreaCode("1");
    orgUnit.setTelephoneNo("25436521");
    orgUnit.setOrgLevelCode('R');
    orgUnit.setOfficeNameCode("RR");
    orgUnit.setRollupRegionNo(12L);
    orgUnit.setRollupRegionCode("19");
    orgUnit.setRollupDistNo(13L);
    orgUnit.setRollupDistCode("25");
    orgUnit.setEffectiveDate(LocalDate.now().minusYears(3L));
    orgUnit.setExpiryDate(LocalDate.now().plusYears(3L));
    orgUnit.setUpdateTimestamp(LocalDate.now());

    mockMvc
        .perform(
            get("/api/codes/org-units")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].orgUnitNo").value(orgUnit.getOrgUnitNo()))
        .andExpect(jsonPath("$[0].orgUnitCode").value(orgUnit.getOrgUnitCode()))
        .andExpect(jsonPath("$[0].orgUnitName").value(orgUnit.getOrgUnitName()))
        .andReturn();
  }

}
