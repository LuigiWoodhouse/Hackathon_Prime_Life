package com.primelife.config;

import com.primelife.exception.KeyVaultException;
import com.primelife.service.KeyFetchService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@Profile("prod")
public class DatabaseConfig {

    @Autowired
    Environment environment;

    @Autowired
    KeyFetchService keyFetchService;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    int poolSize;

    @Value("${spring.datasource.hikari.minimum-idle}")
    int minIdle;

    @Value("${spring.datasource.hikari.idle-timeout.ms}")
    int idleTimeout;

    @Value("${spring.datasource.hikari.max-lifetime.ms}")
    int maxLifetime;

    @Bean
    public DriverManagerDataSource dbConnection() throws KeyVaultException, KeyVaultException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(keyFetchService.fetchDbUsername());
        dataSource.setPassword(keyFetchService.fetchDbPassword());
        return dataSource;
    }

    public HikariDataSource connectionPool() throws KeyVaultException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        config.setUsername(keyFetchService.fetchDbUsername());
        config.setPassword(keyFetchService.fetchDbPassword());
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(poolSize);
        config.setMinimumIdle(minIdle);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setPoolName("MyHikariCPPool");

        return new HikariDataSource(config);
    }
}