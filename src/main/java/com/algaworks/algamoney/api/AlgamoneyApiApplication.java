package com.algaworks.algamoney.api;

import com.algaworks.algamoney.api.config.property.AlgaMoneyApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AlgaMoneyApiProperty.class)
public class AlgamoneyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgamoneyApiApplication.class, args);
    }

}

