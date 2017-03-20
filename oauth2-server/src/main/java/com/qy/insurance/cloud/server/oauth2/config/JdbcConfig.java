package com.qy.insurance.cloud.server.oauth2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/3/16 11:39
 * @version: 1.0.0
 */
@Configuration
public class JdbcConfig {

    @Value("${spring.datasource.type}")
    private String type;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @SuppressWarnings("unchecked")
    public DataSource authDataSource() throws ClassNotFoundException {
        return DataSourceBuilder.create()
                .type((Class<? extends DataSource>) Class.forName(type))
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
