package com.growmighty.lectures.firstday.order.infrastructure.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OrderClientConfig {

    @Bean
    RestClient productRestClient(
            @Value("${order.client.product-base-url:http://localhost:8081}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }

    @Bean
    RestClient paymentRestClient(
            @Value("${order.client.payment-base-url:http://localhost:8082}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
