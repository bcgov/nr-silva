package ca.bc.gov.restapi.results.postgres.configuration;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * This class adds a listener for closing connection gracefully.
 */
@Component
@RequiredArgsConstructor
public class PostgresGracefulShutdownConfiguration implements
    ApplicationListener<ContextClosedEvent> {

  private final EntityManager postgresEntityManager;

  @Override
  public void onApplicationEvent(@NonNull ContextClosedEvent event) {
    postgresEntityManager.close();
  }
}
