package org.example.sushibar.services;

import org.example.sushibar.entities.OrderEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

public interface OrderService {
    List<OrderEntity> getAll();
    Optional<OrderEntity> getById(Long id);
    OrderEntity create(OrderEntity order);
    void delete(Long id);
    Page<OrderEntity> getAllPaged(Pageable pageable);
    OrderEntity update(Long orderId, OrderEntity.OrderStatus newStatus);

}
