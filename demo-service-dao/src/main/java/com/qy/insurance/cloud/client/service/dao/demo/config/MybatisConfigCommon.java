package com.qy.insurance.cloud.client.service.dao.demo.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
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

import javax.sql.DataSource;
import javax.sql.XADataSource;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/24 9:22
 * @version: 1.0.0
 */
@Configuration
public class MybatisConfigCommon {

    public static final String NAME = "common";
    public static final String PREFIX = "database." + NAME;
    public static final String TYPE = "${" + PREFIX + ".type" + "}";

    @Value(TYPE)
    private String dsType;

    @Bean(name = NAME + "DataSource")
    @ConfigurationProperties(prefix = PREFIX)
    @SuppressWarnings("unchecked")
    public DataSource commonDataSource() throws ClassNotFoundException {
        return DataSourceBuilder.create()
                .type((Class<? extends DataSource>) Class.forName(dsType))
                .build();
    }

    @Bean(initMethod = "init" ,destroyMethod = "close")
    public AtomikosDataSourceBean commonAtomikosDataSourceBean() throws ClassNotFoundException {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("commonAtomikosDataSource");
        atomikosDataSourceBean.setXaDataSource((XADataSource) commonDataSource());
        return atomikosDataSourceBean;
    }

    @Bean(name = NAME + "SqlSessionFactory")
    public SqlSessionFactory coreCommonSqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(commonAtomikosDataSourceBean());
        bean.setTypeAliasesPackage(MybatisMapperScannerConfig.BASE_PACKAGE_COMMON);

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources(MybatisMapperScannerConfig.MAPPER_LOCATION_COMMON));
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
}
