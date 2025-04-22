
package org.example.sushibar.controllers;

import com.example.api.ReservationsApi;
import com.example.models.GetAllReservations200Response;
import com.example.models.ReservationRequest;
import com.example.models.ReservationResponse;
import com.example.models.UpdateReservationStatusRequest;
import org.example.sushibar.entities.ReservationEntity;
import org.example.sushibar.mappers.ReservationMapper;
import org.example.sushibar.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.example.sushibar.models.ErrorResponse;
import java.util.NoSuchElementException;
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
    public ResponseEntity<GetAllReservations200Response> getAllReservations(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        var pageResult = reservationService.getAllPaged(pageable);

        GetAllReservations200Response response = new GetAllReservations200Response();
        response.setContent(
                pageResult.getContent().stream()
                        .map(ReservationMapper::toDto)
                        .collect(Collectors.toList())
        );
        response.setNumber(pageResult.getNumber());
        response.setSize(pageResult.getSize());
        response.setTotalPages(pageResult.getTotalPages());
        response.setTotalElements((int) pageResult.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ReservationResponse> getReservationById(Integer reservationId) {
        Optional<ReservationEntity> found = reservationService.getById(reservationId.longValue());
        return found.map(r -> ResponseEntity.ok(ReservationMapper.toDto(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    private boolean isValidReservationTransition(ReservationEntity.ReservationStatus current, ReservationEntity.ReservationStatus next) {
        return switch (current) {
            case PENDING -> next == ReservationEntity.ReservationStatus.CONFIRMED || next == ReservationEntity.ReservationStatus.CANCELLED;
            case CONFIRMED -> next == ReservationEntity.ReservationStatus.SEATED;
            case SEATED -> next == ReservationEntity.ReservationStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }



    @Override
    public ResponseEntity<ReservationResponse> updateReservationStatus(
            Integer reservationId,
            UpdateReservationStatusRequest updateRequest
    ) {
        ReservationEntity reservation = reservationService.getById(reservationId.longValue())
                .orElseThrow(() -> new NoSuchElementException("Reservation not found"));

        var current = reservation.getStatus();
        var next = ReservationEntity.ReservationStatus.valueOf(updateRequest.getStatus().toString().toUpperCase());

        if (!isValidReservationTransition(current, next)) {
            throw new IllegalArgumentException("Invalid status transition from " + current + " to " + next);
        }

        reservation.setStatus(next);
        var updated = reservationService.create(reservation);
        return ResponseEntity.ok(ReservationMapper.toDto(updated));
    }


}


