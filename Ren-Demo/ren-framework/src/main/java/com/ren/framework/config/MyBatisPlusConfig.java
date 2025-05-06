package com.ren.framework.config;

import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Mybatis支持*匹配扫描包
 *
 * @author admin
 */
@Configuration
public class MyBatisPlusConfig
{
    /*
     * 配置分页插件
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @author admin
     * @date 2025/05/06 10:33
     */
    /*@Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }*/

    /*
     * 创建SqlSessionFactory
     * @param dataSource
     * @return org.apache.ibatis.session.SqlSessionFactory
     * @author admin
     * @date 2025/05/06 10:33
     */
    /*@Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setVfs(SpringBootVFS.class);  // 关键配置
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }*/
}