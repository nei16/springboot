package com.nei.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = DataSourceConfig2.BASE_PACKAGES, sqlSessionTemplateRef = DataSourceConfig2.SQL_SESSION_TEMPLATE)
public class DataSourceConfig2 {

    static final String BASE_PACKAGES = "com.nei.mapper.second";
    static final String SQL_SESSION_TEMPLATE = "sqlSessionTemplate2";
    private static final String DATA_SOURCE = "dataSource2";
    private static final String PROPERTIES_PREFIX = "spring.datasource.second";
    private static final String SQL_SESSION_FACTORY = "sqlSessionFactory2";
    private static final String LOCATION_PATTERN = "classpath:mapper/second/*.xml";
    private static final String TYPE_ALIASES_PACKAGE = "com.nei.entity.second";
    private static final String TRANSACTION_MANAGER = "transactionManager2";

    @Autowired
    private DataSource dataSource2;

    @Bean(name = DATA_SOURCE)
    @ConfigurationProperties(prefix = PROPERTIES_PREFIX)
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory2() throws Exception {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(false);
        configuration.setMapUnderscoreToCamelCase(true);

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource2);
        bean.setConfiguration(configuration);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(LOCATION_PATTERN));
        bean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
        return bean.getObject();
    }

    @Bean(name = TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager2() {
        return new DataSourceTransactionManager(dataSource2);
    }

    @Bean(name = SQL_SESSION_TEMPLATE)
    public SqlSessionTemplate sqlSessionTemplate2() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory2());
    }

}
