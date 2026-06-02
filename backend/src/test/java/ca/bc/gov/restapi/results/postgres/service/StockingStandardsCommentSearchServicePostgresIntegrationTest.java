package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.AbstractStockingStandardsCommentSearchServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Stocking Standards Comment Search Service | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class StockingStandardsCommentSearchServicePostgresIntegrationTest
    extends AbstractStockingStandardsCommentSearchServiceIntegrationTest {}
