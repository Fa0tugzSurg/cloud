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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MybatisConfigLink {

    public static final String NAME = "link";
    public static final String PREFIX = "database." + NAME;
    public static final String TYPE = "${" + PREFIX + ".type" + "}";

    @Value(TYPE)
    private String type;

    @Primary
    @Bean(name = NAME + "DataSource")
    @ConfigurationProperties(prefix = PREFIX)
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
        bean.setMapperLocations(resolver.getResources(MybatisMapperScannerConfig.MAPPER_LOCATION_LINK));
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

//    @Bean(name = NAME + "Transaction")
//    public DataSourceTransactionManager dataSourceTransactionManager() throws ClassNotFoundException {
//        return new DataSourceTransactionManager(businessDataSource());
//    }

//    @Bean
//    @Autowired
//    public JtaTransactionManager jtaTransactionManager(UserTransactionManager userTransactionManager,UserTransactionImp userTransactionImp){
//
//    }
}
