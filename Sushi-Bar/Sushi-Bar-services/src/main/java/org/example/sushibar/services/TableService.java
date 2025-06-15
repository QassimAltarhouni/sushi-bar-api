package org.example.sushibar.services;

import org.example.sushibar.entities.TableEntity;

import java.util.List;
import java.util.Optional;

public interface TableService {
    List<TableEntity> getAll();
    Optional<TableEntity> getById(Long id);
    TableEntity create(TableEntity table);
    TableEntity updateAvailability(Long id, boolean available , Integer seats);
}
