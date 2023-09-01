package com.programmingtechie.order.service.service;

import com.programmingtechie.order.service.dto.InventoryResponse;
import com.programmingtechie.order.service.dto.OrderLineItemsDto;
import com.programmingtechie.order.service.dto.OrderRequest;
import com.programmingtechie.order.service.event.OrderPlacedEvent;
import com.programmingtechie.order.service.model.Order;
import com.programmingtechie.order.service.model.OrderLineItems;
import com.programmingtechie.order.service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final Tracer tracer;
    private final KafkaProducer kafkaProducer;

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

        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
        try(Tracer.SpanInScope SpanInScope = tracer.withSpan(inventoryServiceLookup.start())){
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
                OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
                orderPlacedEvent.setOrderNumber(order.getOrderNumber());
                kafkaProducer.sendMessage(orderPlacedEvent);
                return "Order placed successfully";
            } else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        }finally {
            inventoryServiceLookup.end();
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
