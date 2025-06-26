package org.example.sushibar.repositories;

import org.example.sushibar.entities.SemanticMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemanticMetadataRepository extends JpaRepository<SemanticMetadataEntity, Long> {
    Optional<SemanticMetadataEntity> findTopByOrderByUpdatedAtDesc();
}