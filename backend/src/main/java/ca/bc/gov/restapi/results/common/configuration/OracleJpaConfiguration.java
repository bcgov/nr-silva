package ca.bc.gov.restapi.results.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** This class holds JPA configurations for the Oracle database. */
@Configuration
@EnableJpaRepositories(
    basePackages = {"ca.bc.gov.restapi.results.oracle"},
    entityManagerFactoryRef = "oracleEntityManagerFactory",
    transactionManagerRef = "oracleTransactionManager")
@EnableTransactionManagement
public class OracleJpaConfiguration {

  @Value("${ca.bc.gov.nrs.oracle.host}")
  private String oracleHost;

  @Bean(name = "oracleEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
      @Qualifier("oracleDataSource") HikariDataSource dataSource,
      @Qualifier("oracleManagedTypes") PersistenceManagedTypes persistenceManagedTypes,
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(dataSource)
        .properties(
            Map.of(
                "hibernate.dialect",
                "org.hibernate.dialect.OracleDialect",
                "hibernate.boot.allow_jdbc_metadata_access",
                "false",
                "hibernate.hikari.connection.provider_class",
                "org.hibernate.hikaricp.internal.HikariCPConnectionProvider",
                "hibernate.connection.datasource",
                dataSource,
                "hibernate.connection.oracle.net.ssl_server_dn_match",
                "false",
                "hibernate.connection.oracle.net.ssl_key_alias",
                oracleHost))
        .packages("ca.bc.gov.restapi.results.oracle")
        .managedTypes(persistenceManagedTypes)
        .persistenceUnit("oracle")
        .build();
  }

  @Bean(name = "oracleManagedTypes")
  public PersistenceManagedTypes oracleManagedTypes(ResourceLoader resourceLoader) {
    return new PersistenceManagedTypesScanner(resourceLoader)
        .scan("ca.bc.gov.restapi.results.oracle");
  }

  @Bean(name = "oracleDataSource")
  @ConfigurationProperties(prefix = "spring.oracle.hikari")
  public HikariDataSource oracleDataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean(name = "oracleTransactionManager")
  public PlatformTransactionManager oracleTransactionManager(
      @Qualifier("oracleEntityManagerFactory") final EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
