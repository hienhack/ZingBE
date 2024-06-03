package hcmus.zingmp3.core.web.controller;

import hcmus.zingmp3.core.service.artist.ArtistService;
import hcmus.zingmp3.core.web.dto.ArtistRequest;
import hcmus.zingmp3.core.web.dto.ArtistResponse;
import hcmus.zingmp3.core.web.dto.OnCreate;
import hcmus.zingmp3.core.web.dto.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/artists")
@Validated
public class ArtistController {
    private final ArtistService artistService;
    private final String artistUrl = "http://localhost:8080/api/artists/";

    @PostMapping
    @SecurityRequirement(name = "Keycloak")
    public ResponseEntity<ArtistResponse> createArtist(
            @RequestBody @Validated(OnCreate.class) final ArtistRequest request
    ) {

        return ResponseEntity
                .status(CREATED)
                .body(artistService.createArtist(request));
    }

    @GetMapping("/{artist-id}")
    public ResponseEntity<ArtistResponse> getArtistById(
            @PathVariable("artist-id") final UUID id
    ) {
        return ResponseEntity.ok(artistService.getArtistById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArtistResponse>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/page")
    public ResponseEntity<List<ArtistResponse>> getArtistByAlias(
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size,
            @Schema(description = "Sort by field name", allowableValues = { "id", "name", "alias", "realName" })
            @RequestParam(defaultValue = "id") String sortBy,
            @Schema(description = "Sort order", allowableValues = { "asc", "desc" })
            @RequestParam(defaultValue = "asc") String order

    ) {
        Sort sortData = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sortData);
        return ResponseEntity.ok(artistService.getAllArtists(pageable));
    }

    @GetMapping
    public ResponseEntity<ArtistResponse> getArtistByAlias(
            @RequestParam("alias") final String alias
    ) {
        return ResponseEntity.ok(artistService.getArtistByAlias(alias));
    }

    @PutMapping("/approved/{artist-alias}")
    @SecurityRequirement(name = "Keycloak")
    public ResponseEntity<?> approveArtist(
            @PathVariable("artist-alias") final String alias
    ) {
        artistService.approveArtist(alias);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/rejected/{artist-alias}")
    @SecurityRequirement(name = "Keycloak")
    public ResponseEntity<?> rejectArtist(
            @PathVariable("artist-alias") final String alias
    ) {
        artistService.rejectArtist(alias);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    @SecurityRequirement(name = "Keycloak")
    public ResponseEntity<?> updateArtist(
            @RequestBody @Validated({OnUpdate.class}) final ArtistRequest request
    ) {
        return ResponseEntity.ok(artistService.updateArtist(request));
    }

    @DeleteMapping("/{artist-id}")
    @SecurityRequirement(name = "Keycloak")
    public ResponseEntity<?> deleteArtist(
            @PathVariable("artist-id") final UUID id
    ) {
        artistService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }
}
