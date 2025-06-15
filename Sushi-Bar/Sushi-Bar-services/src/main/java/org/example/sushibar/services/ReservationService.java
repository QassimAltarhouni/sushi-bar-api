package org.example.sushibar.services;

import org.example.sushibar.entities.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    /**
     * Retrieve all reservations (non-paginated).
     */
    List<ReservationEntity> getAll();

    /**
     * Retrieve a specific reservation by ID.
     */
    Optional<ReservationEntity> getById(Long id);

    /**
     * Create a new reservation.
     */
    ReservationEntity create(ReservationEntity reservation);

    /**
     * Delete reservation by ID.
     * @return true if reservation existed and was deleted, false if not found.
     */
    boolean delete(Long id);

    /**
     * Retrieve all reservations with pagination.
     */
    Page<ReservationEntity> getAllPaged(Pageable pageable);

    /**
     * Update the status of a specific reservation.
     * (e.g., PENDING → CONFIRMED → SEATED → COMPLETED)
     */
    ReservationEntity update(Long reservationId, ReservationEntity.ReservationStatus newStatus);
}
