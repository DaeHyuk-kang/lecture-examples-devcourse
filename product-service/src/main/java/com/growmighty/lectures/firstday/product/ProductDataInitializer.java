package com.growmighty.lectures.firstday.product;

import com.growmighty.lectures.firstday.product.application.ProductService;
import com.growmighty.lectures.firstday.product.application.dto.RegisterProductCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class ProductDataInitializer implements CommandLineRunner {
    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        // sellerId는 이제 다른 서비스의 식별자일 뿐이므로 임의 값(1L)을 사용
        productService.register(new RegisterProductCommand(1L, "청축 키보드", BigDecimal.valueOf(120_000), 100, "청축 키보드 입니다."));
        productService.register(new RegisterProductCommand(1L, "무선 마우스", BigDecimal.valueOf(45_000), 200, "무선 마우스 " +
            "입니다."));
    }
}
