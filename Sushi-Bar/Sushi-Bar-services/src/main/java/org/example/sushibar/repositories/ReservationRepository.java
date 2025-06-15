package org.example.sushibar.repositories;

import org.example.sushibar.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    boolean existsByTableNumberAndDate(Integer tableNumber, LocalDateTime date);

    List<ReservationEntity> findAllByUserId(Long userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE ReservationEntity r SET r.status = :status WHERE r.id = :id")
    int updateStatusById(@Param("id") Long id,
                         @Param("status") ReservationEntity.ReservationStatus status);
}
