package com.growmighty.lectures.firstday.tangledmonolith.order;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResult> getOrders() {
        return orderService.getOrders();
    }

    @PostMapping
    public OrderResult placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(request.userId(), request.items());
    }

    public record OrderRequest(@NonNull Long userId, @NonNull List<OrderLine> items) {

    }

    public record OrderLine(@NonNull Long productId, @NonNull Integer quantity) {

    }
}
