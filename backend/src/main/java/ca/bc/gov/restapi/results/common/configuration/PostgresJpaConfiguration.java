package ca.bc.gov.restapi.results.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** This class holds JPA configurations for the Postgres database. */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {"ca.bc.gov.restapi.results.postgres"},
    entityManagerFactoryRef = "postgresEntityManagerFactory",
    transactionManagerRef = "postgresTransactionManager")
public class PostgresJpaConfiguration {

  @Bean(name = "postgresManagedTypes")
  @Primary
  public PersistenceManagedTypes postgresManagedTypes(ResourceLoader resourceLoader) {
    // Automatically scan and register all Postgres entity classes (required for native image)
    return new PersistenceManagedTypesScanner(resourceLoader)
        .scan("ca.bc.gov.restapi.results.postgres.entity");
  }

  @Primary
  @Bean(name = "postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
      @Qualifier("postgresHikariDataSource") HikariDataSource dataSource,
      @Qualifier("postgresManagedTypes") PersistenceManagedTypes managedTypes) {

    LocalContainerEntityManagerFactoryBean factoryBean =
        new LocalContainerEntityManagerFactoryBean();
    factoryBean.setDataSource(dataSource);
    // Explicitly set managed types instead of package scanning (required for native images)
    factoryBean.setManagedTypes(managedTypes);
    factoryBean.setPersistenceUnitName("postgres");

    // Set JPA vendor adapter
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    factoryBean.setJpaVendorAdapter(vendorAdapter);

    // Explicitly set EntityManagerFactory interface to avoid conflicts
    factoryBean.setEntityManagerFactoryInterface(EntityManagerFactory.class);

    // Set JPA properties
    factoryBean.setJpaPropertyMap(
        Map.of(
            "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect",
            "hibernate.boot.allow_jdbc_metadata_access", "false",
            "hibernate.hikari.connection.provider_class",
                "org.hibernate.hikaricp.internal.HikariCPConnectionProvider",
            "hibernate.connection.datasource", dataSource,
            "hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"));

    return factoryBean;
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
      @Qualifier("postgresEntityManagerFactory") final EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
