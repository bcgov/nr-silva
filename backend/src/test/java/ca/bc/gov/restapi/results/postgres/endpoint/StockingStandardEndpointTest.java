package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardResponseDto;
import ca.bc.gov.restapi.results.postgres.service.CreateStockingStandardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("Unit Test | StockingStandardEndpoint")
class StockingStandardEndpointTest {

  @Test
  @DisplayName("Delegates the validated request to the create service")
  void createStockingStandard_delegatesToService() {
    CreateStockingStandardService service = Mockito.mock(CreateStockingStandardService.class);
    StockingStandardEndpoint endpoint = new StockingStandardEndpoint(service);
    CreateStockingStandardRequestDto request = Mockito.mock(CreateStockingStandardRequestDto.class);
    CreateStockingStandardResponseDto expected = new CreateStockingStandardResponseDto(123L);
    when(service.create(request)).thenReturn(expected);

    CreateStockingStandardResponseDto actual = endpoint.createStockingStandard(request);

    assertThat(actual).isSameAs(expected);
    verify(service).create(request);
  }
}
