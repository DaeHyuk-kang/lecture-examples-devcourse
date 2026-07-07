package com.growmighty.lectures.firstday.cart.infrastructure.client;

import com.growmighty.lectures.firstday.cart.application.port.ProductPort;
import com.growmighty.lectures.firstday.cart.application.port.dto.ProductSnapshot;
import com.growmighty.lectures.firstday.cart.infrastructure.client.dto.ApiResponseBody;
import com.growmighty.lectures.firstday.cart.infrastructure.client.dto.ProductApiData;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ProductHttpClient implements ProductPort {

    private final RestClient productRestClient;

    @Override
    public ProductSnapshot getProduct(Long productId) {
        ApiResponseBody<ProductApiData> body = productRestClient.get()
                .uri("/products/{productId}", productId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        ProductApiData data = body.data();
        return new ProductSnapshot(data.id(), data.orderable());
    }
}
