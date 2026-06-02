package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointStockingStandardsCommentSearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Stocking Standards Comment Search Endpoint | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class SearchEndpointStockingStandardsCommentSearchPostgresIntegrationTest
    extends AbstractSearchEndpointStockingStandardsCommentSearchIntegrationTest {}
