package org.example.sushibar.controllers;

import com.example.api.OrdersApi;
import com.example.models.GetAllOrders200Response;
import com.example.models.OrderRequest;
import com.example.models.OrderResponse;
import com.example.models.UpdateOrderStatusRequest;
import org.example.sushibar.aop.annotations.LogMethod;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
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
    @LogMethod
    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
        if (orderRequest == null || orderRequest.getItems() == null || orderRequest.getPhone() == null) {
            return ResponseEntity.badRequest().build();
        }

        List<MenuItemEntity> items = menuItemRepository.findAllById(
                orderRequest.getItems().stream()
                        .map(Integer::longValue)
                        .collect(Collectors.toList())
        );

        if (items.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        double totalCost = items.stream().mapToDouble(MenuItemEntity::getPrice).sum();

        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.PENDING);
        order.setItems(items);
        order.setDate(LocalDateTime.now());
        order.setPaid(Boolean.TRUE.equals(orderRequest.getPaid()));
        order.setNotes(orderRequest.getNotes());
        order.setTotalCost(totalCost);
        order.setPhone(orderRequest.getPhone());

        OrderEntity saved = orderService.create(order);
        return ResponseEntity.status(201).body(OrderMapper.toDto(saved));
    }
    @LogMethod
    @Override
    public ResponseEntity<GetAllOrders200Response> getAllOrders(Integer page, Integer size) {
        if (page == null) page = 0;
        if (size == null) size = 10;

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
    @LogMethod
    @Override
    public ResponseEntity<OrderResponse> getOrderById(Integer orderId) {
        if (orderId == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<OrderEntity> found = orderService.getById(orderId.longValue());
        return found.map(order -> ResponseEntity.ok(OrderMapper.toDto(order)))
                .orElse(ResponseEntity.notFound().build());
    }
    @LogMethod
    @Override
    public ResponseEntity<Void> deleteOrder(Integer orderId) {
        if (orderId == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean existed = orderService.delete(orderId.longValue());
        return existed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @LogMethod
    @Override
    public ResponseEntity<OrderResponse> updateOrderStatus(Integer orderId, UpdateOrderStatusRequest request) {
        if (orderId == null || request == null || request.getStatus() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<OrderEntity> found = orderService.getById(orderId.longValue());
        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        OrderEntity order = found.get();
        OrderStatus currentStatus = order.getStatus();

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.getStatus().name());// ⬅️ String-to-enum safely
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        if (!isValidTransition(currentStatus, newStatus)) {
            return ResponseEntity.badRequest().build();
        }

        OrderEntity updated = orderService.update(orderId.longValue(), newStatus);
        return ResponseEntity.ok(OrderMapper.toDto(updated));
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case PENDING -> next == OrderStatus.PREPARING || next == OrderStatus.CANCELLED;
            case PREPARING -> next == OrderStatus.READY;
            case READY -> next == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }
    @LogMethod
    @Override
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(Integer userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<OrderEntity> orders = orderService.getOrdersByUserId(userId.longValue());
        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<OrderResponse> response = orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


}
