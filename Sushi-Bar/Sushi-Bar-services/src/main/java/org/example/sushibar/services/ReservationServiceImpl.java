package org.example.sushibar.services;

import jakarta.transaction.Transactional;
import org.example.sushibar.entities.ReservationEntity;
import org.example.sushibar.entities.UserEntity;
import org.example.sushibar.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;
    private final UserService userService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
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
    public Page<ReservationEntity> getAllPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ReservationEntity create(ReservationEntity reservation) {
        // ✅ تحقق من التاريخ
        if (reservation.getDate() == null) {
            reservation.setDate(LocalDateTime.now().plusHours(1));
        }

        // ✅ تحقق من الهاتف
        if (reservation.getCustomerNumber() == null || reservation.getCustomerNumber().isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        // ✅ تحقق من المستخدم
        Optional<UserEntity> userOpt = userService.getByPhone(reservation.getCustomerNumber());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("No user found with this phone number");
        }

        reservation.setUser(userOpt.get());

        // ✅ منع حجز طاولة محجوزة بالفعل في نفس التاريخ
        boolean conflict = repository.existsByTableNumberAndDate(reservation.getTableNumber(), reservation.getDate());
        if (conflict) {
            throw new IllegalStateException("This table is already reserved at the selected time");
        }

        return repository.save(reservation);
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
    @Override
    @Transactional
    public ReservationEntity update(Long reservationId, ReservationEntity.ReservationStatus newStatus) {
        Optional<ReservationEntity> existingReservation = repository.findById(reservationId);
        if (existingReservation.isEmpty()) {
            throw new IllegalArgumentException("Reservation not found");
        }

        // لو تبي تحدث مباشر بدون save()
        int updatedRows = repository.updateStatusById(reservationId, newStatus);

        if (updatedRows > 0) {
            return repository.findById(reservationId).get(); // نرجع النسخة الجديدة
        } else {
            throw new IllegalStateException("Failed to update status");
        }
    }
}
