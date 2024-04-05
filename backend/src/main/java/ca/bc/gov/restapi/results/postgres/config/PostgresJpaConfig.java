package ca.bc.gov.restapi.results.postgres.config;

import ca.bc.gov.restapi.results.postgres.endpoint.UserOpeningEndpoint;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserOpeningRepository;
import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** This class holds JPA configurations for the Postgres database. */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackageClasses = {
      // endpoints
      UserOpeningEndpoint.class,
      // services
      // none yet
      // repositories
      UserOpeningRepository.class,
      // entities
      UserOpeningEntity.class,
      UserOpeningEndpoint.class
    },
    entityManagerFactoryRef = "postgresEntityManagerFactory",
    transactionManagerRef = "postgresTransactionManager")
public class PostgresJpaConfig {

  /**
   * Creates the EntityManagerFactory for the postgres database.
   *
   * @param dataSource The Postgres DataSource.
   * @param builder The EntityManagerFactory builder
   * @return LocalContainerEntityManagerFactoryBean
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
      @Qualifier("postgresDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
    Properties jpaProps = new Properties();
    jpaProps.setProperty("hibernate.default_schema", "silva");
    jpaProps.setProperty("hibernate.ddl-auto", "update");
    jpaProps.setProperty("defer-datasource-initialization", "true");
    jpaProps.setProperty("sql.init.mode", "always");

    LocalContainerEntityManagerFactoryBean build =
        builder.dataSource(dataSource).packages(getPostgresEntities()).build();
    build.setJpaProperties(jpaProps);
    return build;
  }

  private Class<?>[] getPostgresEntities() {
    return new Class[] {UserOpeningEntity.class};
  }

  @Bean
  public PlatformTransactionManager postgresTransactionManager(
      @Qualifier("postgresEntityManagerFactory")
          LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory) {
    return new JpaTransactionManager(
        Objects.requireNonNull(postgresEntityManagerFactory.getObject()));
  }
}
