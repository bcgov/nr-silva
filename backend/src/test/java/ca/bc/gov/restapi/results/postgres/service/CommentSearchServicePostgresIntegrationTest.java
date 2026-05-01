package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.AbstractCommentSearchServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for CommentSearchService with PostgreSQL database. Executes all comment search
 * service integration tests against the PostgreSQL database.
 */
@DisplayName("Integrated Test | Comment Search Service | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class CommentSearchServicePostgresIntegrationTest
    extends AbstractCommentSearchServiceIntegrationTest {}
