package org.example.sushibar.services;

import org.example.sushibar.entities.SemanticMetadataEntity;
import org.example.sushibar.repositories.SemanticMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class SemanticMetadataServiceImpl implements SemanticMetadataService {
    private final SemanticMetadataRepository repository;

    @Autowired
    public SemanticMetadataServiceImpl(SemanticMetadataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<SemanticMetadataEntity> getLatest() {
        return repository.findTopByOrderByUpdatedAtDesc();
    }

    @Override
    public SemanticMetadataEntity create(String jsonLd) {
        SemanticMetadataEntity entity = new SemanticMetadataEntity();
        entity.setJsonLd(jsonLd);
        entity.setUpdatedAt(Instant.now());
        return repository.save(entity);
    }

    @Override
    public SemanticMetadataEntity update(String jsonLd) {
        SemanticMetadataEntity entity = getLatest().orElseGet(SemanticMetadataEntity::new);
        entity.setJsonLd(jsonLd);
        entity.setUpdatedAt(Instant.now());
        return repository.save(entity);
    }
}