package org.example.sushibar.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "semantic_metadata")
public class SemanticMetadataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String jsonLd;

    private Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJsonLd() { return jsonLd; }
    public void setJsonLd(String jsonLd) { this.jsonLd = jsonLd; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}