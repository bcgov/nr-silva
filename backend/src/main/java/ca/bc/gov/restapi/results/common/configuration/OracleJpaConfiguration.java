package ca.bc.gov.restapi.results.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This class holds JPA configurations for the Oracle database.
 */
@Configuration
@EnableJpaRepositories(
    basePackages = {"ca.bc.gov.restapi.results.oracle"},
    entityManagerFactoryRef = "oracleEntityManagerFactory",
    transactionManagerRef = "oracleTransactionManager"
)
@EnableTransactionManagement
public class OracleJpaConfiguration {

  @Bean(name = "oracleEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
      @Qualifier("oracleDataSource") HikariDataSource dataSource,
      EntityManagerFactoryBuilder builder
  ) {
    return builder
        .dataSource(dataSource)
        .properties(Map.of(
            "hibernate.dialect", "org.hibernate.dialect.OracleDialect",
            "hibernate.boot.allow_jdbc_metadata_access","false",
            "hibernate.hikari.connection.provider_class",
            "org.hibernate.hikaricp.internal.HikariCPConnectionProvider",
            "hibernate.connection.datasource", dataSource
        ))
        .packages("ca.bc.gov.restapi.results.oracle")
        .persistenceUnit("oracle")
        .build();
  }

  @Bean(name = "oracleDataSource")
  @ConfigurationProperties(prefix = "spring.oracle.hikari")
  public HikariDataSource oracleDataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean(name = "oracleTransactionManager")
  public PlatformTransactionManager oracleTransactionManager(
      @Qualifier("oracleEntityManagerFactory") final EntityManagerFactory entityManagerFactory
  ) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
