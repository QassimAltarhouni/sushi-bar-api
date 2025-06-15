package org.example.sushibar.repositories;

import org.example.sushibar.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.sushibar.entities.OrderEntity.OrderStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE OrderEntity o SET o.status = :status WHERE o.id = :orderId")
    int updateStatusById(Long orderId, OrderStatus status);
    List<OrderEntity> findAllByUserId(Long userId);
}