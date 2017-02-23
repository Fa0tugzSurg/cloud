package com.qy.insurance.cloud.client.service.dao.demo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 任务：
 * 描述：
 * 作者：蒋珂
 * 时间：2016/11/9 19:07
 *
 * @version 1.0
 */

@Configuration
@EnableTransactionManagement
public class MybatisConfigLink {

    public static final String NAME = "link";

    @Value("${database.link.type}")
    private String type;

    @Bean(name = NAME + "DataSource")
    @ConfigurationProperties(prefix = "database.link")
    @SuppressWarnings("unchecked")
    public DataSource businessDataSource() throws ClassNotFoundException {
        return DataSourceBuilder.create()
                .type((Class<? extends DataSource>) Class.forName(type))
                .build();
    }

    @Bean(name = NAME + "SqlSessionFactory")
    public SqlSessionFactory coreCommonSqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(businessDataSource());
        bean.setTypeAliasesPackage(MybatisMapperScannerConfig.BASE_PACKAGE_LINK);

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources(MybatisMapperScannerConfig.MAPPER_LOCATION));
        SqlSessionFactory sqlSessionFactory = bean.getObject();
        specialTypeHandlerRegistry(sqlSessionFactory);
        return sqlSessionFactory;
    }

    //特殊类型的处理Handler注册
    private void specialTypeHandlerRegistry(SqlSessionFactory sqlSessionFactory) {
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
    }

    @Bean(name = NAME + "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(coreCommonSqlSessionFactoryBean());
    }

    @Bean(name = NAME + "Transaction")
    public DataSourceTransactionManager dataSourceTransactionManager() throws ClassNotFoundException {
        return new DataSourceTransactionManager(businessDataSource());
    }
}
