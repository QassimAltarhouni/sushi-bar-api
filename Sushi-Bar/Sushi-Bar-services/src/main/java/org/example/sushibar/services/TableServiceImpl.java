package org.example.sushibar.services;

import jakarta.transaction.Transactional;
import org.example.sushibar.entities.TableEntity;
import org.example.sushibar.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableServiceImpl implements TableService {

    private final TableRepository repository;

    @Autowired
    public TableServiceImpl(TableRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TableEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TableEntity> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public TableEntity create(TableEntity table) {
        return repository.save(table);
    }

    @Override
    @Transactional
    public TableEntity updateAvailability(Long id, boolean available , Integer seats) {
        TableEntity table = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Table not found"));
        table.setAvailable(available);
        table.setSeats(seats);
        return repository.save(table);
    }
}
