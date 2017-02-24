package com.qy.insurance.cloud.client.service.dao.demo.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * 任务：
 * 描述：Mapper扫描配置文件
 * 作者：蒋珂
 * 时间：2016/11/9 15:13
 *
 * @version 1.0
 */
@Configuration
public class MybatisMapperScannerConfig {

    public static final String BASE_PACKAGE_LINK = "com.qy.insurance.cloud.dao.mapper.link";
    public static final String MAPPER_LOCATION_LINK = "classpath:mapper/link/**/*Mapper*.xml";

    public static final String BASE_PACKAGE_COMMON = "com.qy.insurance.cloud.dao.mapper.common";
    public static final String MAPPER_LOCATION_COMMON = "classpath:mapper/common/**/*Mapper*.xml";

    private static final String SQL_SESSION_FACTORY = "SqlSessionFactory";

    @Bean
    public MapperScannerConfigurer linkDatabaseMapperScannerConfig() {
        MapperScannerConfigurer mapperScannerConfigurer = getMapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName(MybatisConfigLink.NAME + SQL_SESSION_FACTORY);
        mapperScannerConfigurer.setBasePackage(BASE_PACKAGE_LINK);
        return mapperScannerConfigurer;
    }

    @Bean
    public MapperScannerConfigurer commonDatabaseMapperScannerConfig() {
        MapperScannerConfigurer mapperScannerConfigurer = getMapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName(MybatisConfigCommon.NAME + "SqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(BASE_PACKAGE_COMMON);
        return mapperScannerConfigurer;
    }

    private MapperScannerConfigurer getMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        MapperHelper mapperHelper = new MapperHelper();
        mapperHelper.registerMapper(MySqlMapper.class);
        mapperHelper.registerMapper(Mapper.class);
        mapperScannerConfigurer.setMapperHelper(mapperHelper);
        Properties properties = new Properties();
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

}

