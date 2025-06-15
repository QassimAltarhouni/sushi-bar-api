package org.example.sushibar.repositories;

import org.example.sushibar.models.MethodStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MethodStatRepository extends JpaRepository<MethodStat, Long> {
    Optional<MethodStat> findByMethodName(String methodName);
}