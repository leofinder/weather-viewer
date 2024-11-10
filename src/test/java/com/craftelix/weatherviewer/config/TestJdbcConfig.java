package com.craftelix.weatherviewer.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(basePackages = "com.craftelix.weatherviewer.repository")
@EnableTransactionManagement
public class TestJdbcConfig extends AbstractJdbcConfiguration {

    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:16.4")
            .withInitScript("db/migration/init.sql");

    @PostConstruct
    public void startContainer() {
        POSTGRE_SQL_CONTAINER.start();
    }

    @PreDestroy
    public void stopContainer() {
        POSTGRE_SQL_CONTAINER.stop();
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(POSTGRE_SQL_CONTAINER.getDriverClassName());
        dataSource.setJdbcUrl(POSTGRE_SQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRE_SQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRE_SQL_CONTAINER.getPassword());
        return dataSource;
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
