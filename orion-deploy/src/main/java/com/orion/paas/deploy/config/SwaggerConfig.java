package com.orion.paas.deploy.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2 配置
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/16 10:05
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class SwaggerConfig {

    /**
     * Swagger配置
     *
     * @return ignore
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.orion.paas.deploy")).paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建API
     *
     * @return API 信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("Orion Deploy Api")
                // 创建人信息
                .contact(new Contact("Li Jiahang", "https://github.com/lijiahangmax", "lijiahang@bjucloud.com"))
                // 版本号
                .version("1.0")
                // 描述
                .description("Orion 自动部署 API")
                .build();
    }

}
