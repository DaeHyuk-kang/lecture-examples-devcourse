package com.growmighty.lectures.firstday.cart.infrastructure.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CartClientConfig {

    @Bean
    RestClient productRestClient(
            @Value("${cart.client.product-base-url:http://localhost:8081}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
