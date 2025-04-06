package org.example.sushibar.controllers;

import com.example.api.OrdersApi;
import com.example.models.OrderRequest;
import com.example.models.OrderResponse;
import org.example.sushibar.entities.MenuItemEntity;
import org.example.sushibar.entities.OrderEntity;
import org.example.sushibar.mappers.OrderMapper;
import org.example.sushibar.repositories.MenuItemRepository;
import org.example.sushibar.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class OrdersController implements OrdersApi {

    private final OrderService orderService;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public OrdersController(OrderService orderService, MenuItemRepository menuItemRepository) {
        this.orderService = orderService;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
        List<MenuItemEntity> items = menuItemRepository.findAllById(orderRequest.getItems()
                .stream()
                .map(Integer::longValue)
                .collect(Collectors.toList()));

        OrderEntity order = new OrderEntity();
        order.setCustomerNumber(orderRequest.getCustomerNumber());
        order.setStatus("Pending");
        order.setItems(items);

        OrderEntity saved = orderService.create(order);
        return ResponseEntity.status(201).body(OrderMapper.toDto(saved));
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAll()
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(Integer orderId) {
        Optional<OrderEntity> found = orderService.getById(orderId.longValue());
        return found.map(order -> ResponseEntity.ok(OrderMapper.toDto(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Integer orderId) {
        orderService.delete(orderId.longValue());
        return ResponseEntity.noContent().build();
    }
}
