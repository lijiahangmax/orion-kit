package com.orion.paas.deploy.config;

import com.orion.spring.Springs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Orion String 配置
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/16 0:57
 */
@Configuration
public class OrionSpringConfig {

    @Bean
    public Springs.ApplicationContextAwareStore springs() {
        return new Springs.ApplicationContextAwareStore();
    }

}
