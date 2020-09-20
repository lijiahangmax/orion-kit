package com.orion.paas.deploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class OrionDeployApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrionDeployApplication.class, args);
    }

}
