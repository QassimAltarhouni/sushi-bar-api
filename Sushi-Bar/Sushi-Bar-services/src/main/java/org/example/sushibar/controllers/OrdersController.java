package org.example.sushibar.controllers;

import com.example.api.OrdersApi;
import com.example.models.GetAllOrders200Response;
import com.example.models.OrderRequest;
import com.example.models.OrderResponse;
import com.example.models.UpdateOrderStatusRequest;
import org.example.sushibar.entities.MenuItemEntity;
import org.example.sushibar.entities.OrderEntity;
import org.example.sushibar.entities.OrderEntity.OrderStatus;
import org.example.sushibar.mappers.OrderMapper;
import org.example.sushibar.repositories.MenuItemRepository;
import org.example.sushibar.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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
        List<MenuItemEntity> items = menuItemRepository.findAllById(
                orderRequest.getItems().stream()
                        .map(Integer::longValue)
                        .collect(Collectors.toList())
        );

        OrderEntity order = new OrderEntity();
        order.setCustomerNumber(orderRequest.getCustomerNumber());
        order.setStatus(OrderStatus.PENDING); // ✅ Using enum directly
        order.setItems(items);

        OrderEntity saved = orderService.create(order);
        return ResponseEntity.status(201).body(OrderMapper.toDto(saved));
    }

    @Override
    public ResponseEntity<GetAllOrders200Response> getAllOrders(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderEntity> pageResult = orderService.getAllPaged(pageable);

        GetAllOrders200Response response = new GetAllOrders200Response();
        response.setContent(
                pageResult.getContent().stream()
                        .map(OrderMapper::toDto)
                        .collect(Collectors.toList())
        );
        response.setNumber(pageResult.getNumber());
        response.setSize(pageResult.getSize());
        response.setTotalPages(pageResult.getTotalPages());
        response.setTotalElements((int) pageResult.getTotalElements());

        return ResponseEntity.ok(response);
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

    @Override
    public ResponseEntity<OrderResponse> updateOrderStatus(Integer orderId, UpdateOrderStatusRequest request) {
        Optional<OrderEntity> found = orderService.getById(orderId.longValue());
        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        OrderEntity order = found.get();
        OrderStatus currentStatus = order.getStatus();

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.getStatus().name());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Invalid enum
        }

        if (!isValidTransition(currentStatus, newStatus)) {
            return ResponseEntity.badRequest().build(); // Invalid transition
        }

        OrderEntity updated = orderService.update(orderId.longValue(), newStatus);
        return ResponseEntity.ok(OrderMapper.toDto(updated));
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case PENDING:
                return next == OrderStatus.PREPARING || next == OrderStatus.CANCELLED;
            case PREPARING:
                return next == OrderStatus.READY;
            case READY:
                return next == OrderStatus.DELIVERED;
            case DELIVERED:
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }
}
