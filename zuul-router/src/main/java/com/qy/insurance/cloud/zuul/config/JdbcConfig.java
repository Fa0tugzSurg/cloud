package com.qy.insurance.cloud.zuul.config;

import com.qy.insurance.cloud.core.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

    public static final String NAME = "datasource";
    public static final String PREFIX = "spring." + NAME;

    @Bean
    @ConfigurationProperties(prefix = PREFIX)
    public DataSourceProperties commonDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = NAME + "DataSource")
    public DataSource commonDataSource() {
        return commonDataSourceProperties().generateDatasource();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
