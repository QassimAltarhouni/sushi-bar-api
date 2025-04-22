
package org.example.sushibar.services;

import org.example.sushibar.entities.ReservationEntity;
import org.example.sushibar.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ReservationEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ReservationEntity> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ReservationEntity create(ReservationEntity reservation) {
        return repository.save(reservation);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<ReservationEntity> getAllPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ReservationEntity updateStatus(Long reservationId, String status) {
        ReservationEntity reservation = getById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found"));

        try {
            reservation.setStatus(ReservationEntity.ReservationStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        return repository.save(reservation);
    }
}
