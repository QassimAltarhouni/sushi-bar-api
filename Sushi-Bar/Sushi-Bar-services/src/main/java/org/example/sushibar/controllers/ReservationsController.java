package org.example.sushibar.controllers;

import com.example.api.ReservationsApi;
import com.example.models.ReservationRequest;
import com.example.models.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ReservationsController implements ReservationsApi {

    private final List<ReservationResponse> reservations = new ArrayList<>();

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ReservationsApi.super.getRequest();
    }

    @Override
    public ResponseEntity<ReservationResponse> createReservation(ReservationRequest reservationRequest) {
        ReservationResponse response = new ReservationResponse()
                .reservationId(reservations.size() + 1)
                .date(reservationRequest.getDate())
                .status("confirmed");
        reservations.add(response);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<Void> cancelReservation(Integer reservationId) {
        reservations.removeIf(r -> r.getReservationId().equals(reservationId));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteAllReservations() {
        reservations.clear();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservations);
    }

    @Override
    public ResponseEntity<ReservationResponse> getReservationById(Integer reservationId) {
        return reservations.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
