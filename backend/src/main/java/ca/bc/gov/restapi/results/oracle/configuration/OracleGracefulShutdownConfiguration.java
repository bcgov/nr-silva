package ca.bc.gov.restapi.results.oracle.configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/** This class adds a listener for closing connection gracefully. */
@Component
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public class OracleGracefulShutdownConfiguration
    implements ApplicationListener<ContextClosedEvent> {

  @PersistenceContext(unitName = "oracle")
  private EntityManager oracleEntityManager;

  @Override
  public void onApplicationEvent(@NonNull ContextClosedEvent event) {
    oracleEntityManager.close();
  }
}
