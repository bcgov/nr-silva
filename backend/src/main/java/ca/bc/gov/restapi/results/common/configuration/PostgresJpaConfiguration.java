package ca.bc.gov.restapi.results.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This class holds JPA configurations for the Postgres database.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {"ca.bc.gov.restapi.results.postgres"},
    entityManagerFactoryRef = "postgresEntityManagerFactory",
    transactionManagerRef = "postgresTransactionManager")
public class PostgresJpaConfiguration {

  @Primary
  @Bean(name = "postgresEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
      @Qualifier("postgresHikariDataSource") HikariDataSource dataSource,
      @Qualifier("postgresManagedTypes") PersistenceManagedTypes persistenceManagedTypes,
      EntityManagerFactoryBuilder builder
  ) {
    return builder
        .dataSource(dataSource)
        .properties(Map.of(
            "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect",
            "hibernate.boot.allow_jdbc_metadata_access", "false",
            "hibernate.hikari.connection.provider_class",
            "org.hibernate.hikaricp.internal.HikariCPConnectionProvider",
            "hibernate.connection.datasource", dataSource
        ))
        .packages("ca.bc.gov.restapi.results.postgres")
        .managedTypes(persistenceManagedTypes)
        .persistenceUnit("postgres")
        .build();
  }

  @Bean(name = "postgresManagedTypes")
  @Primary
  public PersistenceManagedTypes postgresManagedTypes(ResourceLoader resourceLoader) {
    return new PersistenceManagedTypesScanner(resourceLoader)
        .scan("ca.bc.gov.restapi.results.postgres");
  }

  @Bean(name = "postgresHikariDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  @Primary
  public HikariDataSource postgresHikariDataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean(name = "postgresTransactionManager")
  @Primary
  public PlatformTransactionManager postgresTransactionManager(
      @Qualifier("postgresEntityManagerFactory") final EntityManagerFactory entityManagerFactory
  ) {
    return new JpaTransactionManager(entityManagerFactory);
  }

}
