package org.example.sushibar.services;

import org.example.sushibar.entities.ReservationEntity;
import org.example.sushibar.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
