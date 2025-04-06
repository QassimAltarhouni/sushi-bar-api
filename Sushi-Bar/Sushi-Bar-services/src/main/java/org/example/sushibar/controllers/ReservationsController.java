package org.example.sushibar.controllers;

import com.example.api.ReservationsApi;
import com.example.models.ReservationRequest;
import com.example.models.ReservationResponse;
import org.example.sushibar.entities.ReservationEntity;
import org.example.sushibar.mappers.ReservationMapper;
import org.example.sushibar.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ReservationsController implements ReservationsApi {

    private final ReservationService reservationService;

    @Autowired
    public ReservationsController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public ResponseEntity<ReservationResponse> createReservation(ReservationRequest request) {
        ReservationEntity entity = ReservationMapper.toEntity(request);
        ReservationEntity saved = reservationService.create(entity);
        return ResponseEntity.status(201).body(ReservationMapper.toDto(saved));
    }

    @Override
    public ResponseEntity<Void> cancelReservation(Integer reservationId) {
        reservationService.delete(reservationId.longValue());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteAllReservations() {
        reservationService.getAll().forEach(r -> reservationService.delete(r.getId()));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> list = reservationService.getAll().stream()
                .map(ReservationMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<ReservationResponse> getReservationById(Integer reservationId) {
        Optional<ReservationEntity> found = reservationService.getById(reservationId.longValue());
        return found.map(r -> ResponseEntity.ok(ReservationMapper.toDto(r)))
                .orElse(ResponseEntity.notFound().build());
    }
}
