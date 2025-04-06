package org.example.sushibar.services;

import org.example.sushibar.entities.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderEntity> getAll();
    Optional<OrderEntity> getById(Long id);
    OrderEntity create(OrderEntity order);
    void delete(Long id);
}
