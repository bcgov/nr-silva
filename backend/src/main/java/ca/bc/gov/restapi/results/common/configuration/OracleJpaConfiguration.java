package ca.bc.gov.restapi.results.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
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
      @Qualifier("oracleDataSource") HikariDataSource dataSource) {

    LocalContainerEntityManagerFactoryBean factoryBean =
        new LocalContainerEntityManagerFactoryBean();
    factoryBean.setDataSource(dataSource);
    // Package scanning for entity discovery
    factoryBean.setPackagesToScan("ca.bc.gov.restapi.results.oracle.entity");
    factoryBean.setPersistenceUnitName("oracle");

    // Set JPA vendor adapter
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    factoryBean.setJpaVendorAdapter(vendorAdapter);

    // Explicitly set EntityManagerFactory interface to avoid conflicts
    factoryBean.setEntityManagerFactoryInterface(EntityManagerFactory.class);

    // Set JPA properties
    factoryBean.setJpaPropertyMap(
        Map.of(
            "hibernate.dialect", "org.hibernate.dialect.OracleDialect",
            "hibernate.boot.allow_jdbc_metadata_access", "false",
            "hibernate.hikari.connection.provider_class",
                "org.hibernate.hikaricp.internal.HikariCPConnectionProvider",
            "hibernate.connection.datasource", dataSource,
            "hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy",
            "hibernate.connection.oracle.net.ssl_server_dn_match", "false",
            "hibernate.connection.oracle.net.ssl_key_alias", oracleHost,
            "hibernate.jpa.compliance.query", "false"));

    return factoryBean;
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
