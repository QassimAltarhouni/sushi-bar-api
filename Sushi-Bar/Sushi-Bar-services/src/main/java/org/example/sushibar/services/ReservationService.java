package org.example.sushibar.services;

import org.example.sushibar.entities.ReservationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ReservationService {
    List<ReservationEntity> getAll();
    Optional<ReservationEntity> getById(Long id);
    ReservationEntity create(ReservationEntity reservation);
    void delete(Long id);
    Page<ReservationEntity> getAllPaged(Pageable pageable);
    ReservationEntity updateStatus(Long reservationId, String status);
}

