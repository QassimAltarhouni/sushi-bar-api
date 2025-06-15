package org.example.sushibar.repositories;

import org.example.sushibar.entities.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE TableEntity t SET t.available = :available WHERE t.id = :id")
    int updateAvailabilityById(@Param("id") Long id, @Param("available") boolean available);
}
