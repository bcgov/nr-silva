package ca.bc.gov.restapi.results.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.beans.factory.config.ConstructorArgumentValues;

/**
 * Forces multi-datasource repositories to use the correct shared EntityManager in native AOT.
 */
@Configuration
public class JpaRepositoryBeanCustomizer {

  private static final String ORACLE_BASE_PACKAGE = "ca.bc.gov.restapi.results.oracle";
  private static final String POSTGRES_BASE_PACKAGE = "ca.bc.gov.restapi.results.postgres";

  @Bean
  public static BeanFactoryPostProcessor repositoryEntityManagerCustomizer() {
    return new BeanFactoryPostProcessor() {
      @Override
      public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
          throws BeansException {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
          BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
          if (!JpaRepositoryFactoryBean.class.getName().equals(beanDefinition.getBeanClassName())) {
            continue;
          }
          String repositoryClass = extractRepositoryInterface(beanDefinition);
          if (repositoryClass == null) {
            continue;
          }
          if (repositoryClass.startsWith(ORACLE_BASE_PACKAGE)) {
            beanDefinition.getPropertyValues()
                .add("entityManager", new RuntimeBeanReference("jpaSharedEM_oracleEntityManagerFactory"));
            beanDefinition.getPropertyValues().add("transactionManager", "oracleTransactionManager");
          } else if (repositoryClass.startsWith(POSTGRES_BASE_PACKAGE)) {
            beanDefinition.getPropertyValues()
                .add("entityManager", new RuntimeBeanReference("jpaSharedEM_postgresEntityManagerFactory"));
            beanDefinition.getPropertyValues().add("transactionManager", "postgresTransactionManager");
          }
        }
      }
    };
  }

  private static String extractRepositoryInterface(BeanDefinition beanDefinition) {
    ConstructorArgumentValues constructorArgs = beanDefinition.getConstructorArgumentValues();
    ConstructorArgumentValues.ValueHolder holder =
        constructorArgs.getIndexedArgumentValue(0, String.class, null);
    if (holder == null) {
      return null;
    }
    Object value = holder.getValue();
    return value instanceof String ? (String) value : null;
  }
}
