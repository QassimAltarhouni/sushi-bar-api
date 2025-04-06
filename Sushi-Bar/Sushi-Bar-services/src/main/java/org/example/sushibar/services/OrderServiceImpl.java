package org.example.sushibar.services;

import org.example.sushibar.entities.OrderEntity;
import org.example.sushibar.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
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
    public OrderEntity create(OrderEntity order) {
        return repository.save(order);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
