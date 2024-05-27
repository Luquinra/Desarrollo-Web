package com.luisaq.spacetrader;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public ApplicationRunner boot() {
        return args -> {
            // Disable Initializer
        };
    }
}
