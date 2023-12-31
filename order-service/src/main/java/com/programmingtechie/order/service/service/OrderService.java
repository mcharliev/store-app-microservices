package com.programmingtechie.order.service.service;

import com.programmingtechie.order.service.dto.InventoryResponse;
import com.programmingtechie.order.service.dto.OrderLineItemsDto;
import com.programmingtechie.order.service.dto.OrderRequest;
import com.programmingtechie.order.service.event.OrderPlacedEvent;
import com.programmingtechie.order.service.model.Order;
import com.programmingtechie.order.service.model.OrderLineItems;
import com.programmingtechie.order.service.model.User;
import com.programmingtechie.order.service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaProducer kafkaProducer;
    private final UserService userService;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

            //Call to Inventory Service, and place order if product is in stock
            InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                    .allMatch(InventoryResponse::isInStock);
            if (allProductsInStock) {
                orderRepository.save(order);
                User authUser = userService.getUser();
                OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
                orderPlacedEvent.setOrderNumber(order.getOrderNumber());
                orderPlacedEvent.setUserEmail(authUser.getUsername());
                kafkaProducer.sendMessage(orderPlacedEvent);
                return "Order placed successfully";
            } else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setId(orderLineItemsDto.getId());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
