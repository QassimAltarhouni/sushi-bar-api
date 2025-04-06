package org.example.sushibar.services;

import org.example.sushibar.entities.ReservationEntity;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    List<ReservationEntity> getAll();
    Optional<ReservationEntity> getById(Long id);
    ReservationEntity create(ReservationEntity reservation);
    void delete(Long id);
}
