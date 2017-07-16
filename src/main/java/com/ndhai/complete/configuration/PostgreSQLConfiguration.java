package com.ndhai.complete.configuration;

import org.apache.tomcat.jdbc.pool.DataSourceProxy;
import org.apache.tomcat.jdbc.pool.jmx.ConnectionPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by nduyhai on 7/16/2017.
 */
@Configuration
public class PostgreSQLConfiguration {

    @Bean
    @ConditionalOnExpression("${app.datasource.postgresql.jmxEnabled:true}")
    public ConnectionPool getPostgreSQLJmxPool(@Qualifier("PostgreSQLDataSource") DataSource dataSource) throws SQLException {
        return ((DataSourceProxy) dataSource).createPool().getJmxPool();
    }


    @Primary
    @Bean
    @ConfigurationProperties(prefix = "app.datasource.postgresql")
    public DataSource PostgreSQLDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager PostgreSQLTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(PostgreSQLDataSource());
        return transactionManager;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(PostgreSQLDataSource());
        entityManagerFactoryBean.setPersistenceUnitName("PostgreSQLPersistentUnit");
        return entityManagerFactoryBean;
    }
}
