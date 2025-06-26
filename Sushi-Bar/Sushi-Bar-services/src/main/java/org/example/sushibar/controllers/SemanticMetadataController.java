package org.example.sushibar.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.sushibar.entities.SemanticMetadataEntity;
import org.example.sushibar.services.SemanticMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/semantic")
public class SemanticMetadataController {

    /**
     * Minimal fallback JSON-LD returned when no metadata is stored.
     * Users are expected to create metadata via the API.
     */
    private static final String EMPTY_JSON_LD = "{}";

    private final SemanticMetadataService service;

    @Autowired
    public SemanticMetadataController(SemanticMetadataService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get restaurant semantic metadata",
            description = "Returns JSON-LD describing the restaurant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JSON-LD metadata",
                            content = @Content(mediaType = "application/ld+json"))
            }
    )
    @GetMapping(value = "/restaurant", produces = "application/ld+json")
    public ResponseEntity<String> getRestaurantMetadata() {
        String jsonLd = service.getLatest()
                .map(SemanticMetadataEntity::getJsonLd)
                .orElse(EMPTY_JSON_LD);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/ld+json"))
                .body(jsonLd);
    }

    @Operation(summary = "Create restaurant semantic metadata",
            description = "Stores new JSON-LD metadata",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created",
                            content = @Content(mediaType = "application/ld+json"))
            })
    @PostMapping(value = "/restaurant", consumes = "application/ld+json", produces = "application/ld+json")
    public ResponseEntity<String> createRestaurantMetadata(@RequestBody String jsonLd) {
        SemanticMetadataEntity created = service.create(jsonLd);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.valueOf("application/ld+json"))
                .body(created.getJsonLd());
    }

    @Operation(summary = "Update restaurant semantic metadata",
            description = "Updates existing JSON-LD metadata",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated",
                            content = @Content(mediaType = "application/ld+json"))
            })
    @PutMapping(value = "/restaurant", consumes = "application/ld+json", produces = "application/ld+json")
    public ResponseEntity<String> updateRestaurantMetadata(@RequestBody String jsonLd) {
        SemanticMetadataEntity updated = service.update(jsonLd);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/ld+json"))
                .body(updated.getJsonLd());
    }

}