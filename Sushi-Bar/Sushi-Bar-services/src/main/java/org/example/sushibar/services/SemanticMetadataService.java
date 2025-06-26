package org.example.sushibar.services;

import org.example.sushibar.entities.SemanticMetadataEntity;
import java.util.Optional;

public interface SemanticMetadataService {
    /**
     * Returns the most recently updated metadata if present.
     */
    Optional<SemanticMetadataEntity> getLatest();

    /**
     * Stores a new metadata entry with the given JSON-LD string.
     */
    SemanticMetadataEntity create(String jsonLd);

    /**
     * Updates the latest metadata entry or creates one if none exist.
     */
    SemanticMetadataEntity update(String jsonLd);
}