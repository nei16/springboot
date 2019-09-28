package com.nei.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = DataSourceConfig1.BASE_PACKAGES, sqlSessionTemplateRef = DataSourceConfig1.SQL_SESSION_TEMPLATE)
public class DataSourceConfig1 {

    static final String BASE_PACKAGES = "com.nei.mapper.first";
    static final String SQL_SESSION_TEMPLATE = "sqlSessionTemplate1";
    private static final String DATA_SOURCE = "dataSource1";
    private static final String PROPERTIES_PREFIX = "spring.datasource.first";
    private static final String SQL_SESSION_FACTORY = "sqlSessionFactory1";
    private static final String LOCATION_PATTERN = "classpath:mapper/first/*.xml";
    private static final String TYPE_ALIASES_PACKAGE = "com.nei.entity.first";
    private static final String TRANSACTION_MANAGER = "transactionManager1";

    @Autowired
    private DataSource dataSource1;

    @Bean(name = DATA_SOURCE)
    @ConfigurationProperties(prefix = PROPERTIES_PREFIX)
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory1() throws Exception {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(false);
        configuration.setMapUnderscoreToCamelCase(true);

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource1);
        bean.setConfiguration(configuration);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(LOCATION_PATTERN));
        bean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
        return bean.getObject();
    }

    @Bean(name = TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager1() {
        return new DataSourceTransactionManager(dataSource1);
    }

    @Bean(name = SQL_SESSION_TEMPLATE)
    public SqlSessionTemplate sqlSessionTemplate1(@Qualifier(SQL_SESSION_FACTORY) SqlSessionFactory sqlSessionFactory1) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory1);
    }

}
