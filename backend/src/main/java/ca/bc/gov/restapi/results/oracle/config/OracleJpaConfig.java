package ca.bc.gov.restapi.results.oracle.config;

import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.repository.CutBlockOpenAdminRepository;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.service.CutBlockOpenAdminService;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** This class holds JPA configurations for the Oracle database. */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackageClasses = {
      // endpoints
      CutBlockOpenAdminEntity.class,
      // services
      CutBlockOpenAdminService.class,
      OpeningService.class,
      // repositories
      CutBlockOpenAdminRepository.class,
      OpeningRepository.class,
      // entities
      OpeningEntity.class,
      OpeningRepository.class,
    },
    entityManagerFactoryRef = "oracleEntityManagerFactory",
    transactionManagerRef = "oracleTransactionManager")
public class OracleJpaConfig {

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
      @Qualifier("oracleDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
    return builder.dataSource(dataSource).packages(getOracleEntities()).build();
  }

  private Class<?>[] getOracleEntities() {
    return new Class[] {CutBlockOpenAdminEntity.class, OpeningEntity.class};
  }

  @Bean
  public PlatformTransactionManager oracleTransactionManager(
      @Qualifier("oracleEntityManagerFactory")
          LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory) {
    return new JpaTransactionManager(
        Objects.requireNonNull(oracleEntityManagerFactory.getObject()));
  }
}
