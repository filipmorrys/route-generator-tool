package es.indra.tools.route.generator.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import es.indra.tools.route.generator.RouteGeneratorToolApplication;
import es.indra.tools.route.generator.dto.train.Train;

/**
 * The Class DatabaseConfig.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  basePackageClasses = RouteGeneratorToolApplication.class,
  entityManagerFactoryRef = "trainEntityManagerFactory"
  //transactionManagerRef = "trainTransactionManager"
)
public class DatabaseConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);

  @Bean("topologyDataSourceProperties")
  @ConfigurationProperties(prefix = "spring.datasource.topology")
  public DataSourceProperties topologyDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean("trainDataSourceProperties")
  @ConfigurationProperties(prefix = "spring.datasource.train")
  public DataSourceProperties trainDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean("topologyDataSource")
  public DataSource topologyDataSource(@Qualifier("topologyDataSourceProperties") DataSourceProperties properties) {
    LOGGER.info("Topology DataSourceProperties: " + properties.toString());
    return properties
        .initializeDataSourceBuilder().build();
  }

  @Bean("trainDataSource")
  @Primary
  public DataSource trainDataSource(@Qualifier("trainDataSourceProperties") DataSourceProperties properties) {
    LOGGER.info("Train DataSourceProperties: " + properties.toString());
    return properties
        .initializeDataSourceBuilder().build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean trainEntityManagerFactory(
    @Qualifier("trainDataSource") DataSource dataSource,
    EntityManagerFactoryBuilder builder) {

    return builder
        .dataSource(dataSource)
        .packages(Train.class)
        .build();
  }

}
