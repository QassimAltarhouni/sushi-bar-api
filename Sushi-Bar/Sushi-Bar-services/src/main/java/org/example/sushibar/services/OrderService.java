package org.example.sushibar.services;

import org.example.sushibar.entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    /**
     * Retrieve all orders (non-paginated).
     */
    List<OrderEntity> getAll();

    /**
     * Retrieve a specific order by ID.
     */
    List<OrderEntity> getOrdersByUserId(Long userId);
    Optional<OrderEntity> getById(Long id);

    /**
     * Create a new order.
     */
    OrderEntity create(OrderEntity order);

    /**
     * Delete order by ID.
     * @return true if order existed and was deleted, false if not found.
     */
    boolean delete(Long id);

    /**
     * Retrieve all orders with pagination.
     */
    Page<OrderEntity> getAllPaged(Pageable pageable);

    /**
     * Update the status of a specific order.
     */
    OrderEntity update(Long orderId, OrderEntity.OrderStatus newStatus);
}
