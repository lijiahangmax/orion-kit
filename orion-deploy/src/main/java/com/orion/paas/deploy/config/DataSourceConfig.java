package com.orion.paas.deploy.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.orion.utils.collect.Lists;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/16 0:33
 */
@Configuration
@MapperScan("com.orion.paas.deploy.dao")
@EnableTransactionManagement
public class DataSourceConfig {

    /**
     * druid连接池
     *
     * @return ignore
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid() {
        return new DruidDataSource();
    }

    /**
     * druid-web
     *
     * @return ignore
     */
    @Bean
    public ServletRegistrationBean statDruidWeb() {
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", "admin");
        initParams.put("loginPassword", "123456");
        // 默认就是允许所有访问
        initParams.put("allow", "");
        initParams.put("deny", "localhost");
        bean.setInitParameters(initParams);
        return bean;
    }

    /**
     * druid-filter
     *
     * @return ignore
     */
    @Bean
    public FilterRegistrationBean statDruidFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();
        initParams.put("exclusions", "*.js,*.css,/druid/*");
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Lists.of("/*"));
        return bean;
    }

}
