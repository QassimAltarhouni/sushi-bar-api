package org.example.sushibar.services;

import org.example.sushibar.entities.OrderEntity;
import org.example.sushibar.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.example.sushibar.services.UserService;
import org.example.sushibar.entities.UserEntity;

import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }



    @Override
    public List<OrderEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<OrderEntity> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<OrderEntity> getOrdersByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }


    @Override
    public OrderEntity create(OrderEntity order) {
        // 💡 التأكد من أن createdOn موجود (لحل مشكلة الـ 500 إذا نُسيت)
        if (order.getCreatedOn() == null) {
            order.setCreatedOn(java.time.LocalDateTime.now());
        }

        if (order.getPhone() == null || order.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }

        Optional<UserEntity> userOpt = userService.getByPhone(order.getPhone());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("No user found with this phone");
        }

        order.setUser(userOpt.get()); // ✅ ربط المستخدم
        return repository.save(order);
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }




    @Override
    public Page<OrderEntity> getAllPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override


    public OrderEntity update(Long orderId, OrderEntity.OrderStatus newStatus) {
        Optional<OrderEntity> existingOrder = repository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }

        int updatedRows = repository.updateStatusById(orderId, newStatus);
        if (updatedRows > 0) {
            return repository.findById(orderId).get(); // Return updated entity
        } else {
            throw new IllegalStateException("Failed to update status");
        }
    }



}
