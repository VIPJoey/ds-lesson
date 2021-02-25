/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.mmc.lesson.member.support;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * MemberDataSourceConfiguration.
 *
 * @author VIPJoey
 * @date 2021/2/25 6:00 下午
 */
@Configuration
@MapperScan(basePackages = {
        "com.mmc.lesson.member.mapper"}, sqlSessionTemplateRef = "hakiriSqlSessionTemplate")
public class MemberDataSourceConfiguration {

    /**
     * 迁移过程中存在两份数据源，这里通过前缀来区分.
     */
    @Bean(name = "hakiriDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * 会话工厂.
     */
    @Bean(name = "hakiriSqlSessionFactory")
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("hakiriDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mysqlMappers/*.xml"));
        bean.setVfs(SpringBootVFS.class);
        return bean.getObject();
    }

    /**
     * 目前只有单元测试用到事务.
     */
    @Bean(name = "hakiriTransactionManager")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("hakiriDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * mybatis用到会话.
     */
    @Bean(name = "hakiriSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("hakiriSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
