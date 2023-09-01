package com.programmingtechie.order.service.controller;

import com.programmingtechie.order.service.dto.OrderRequest;
import com.programmingtechie.order.service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    public String fallbackMethod(@RequestBody OrderRequest orderRequest, RuntimeException exception) {
        return "Oops!!! Something went wrong, please order after some time!";
    }
}
