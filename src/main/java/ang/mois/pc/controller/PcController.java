package ang.mois.pc.controller;

import ang.mois.pc.dto.request.PcRequestDto;
import ang.mois.pc.dto.response.PcResponseDto;
import ang.mois.pc.service.PcService;
import ang.mois.pc.validation.ValidationGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for computer (PC) CRUD operations
 */
@RestController
@RequestMapping("/computer")
public class PcController {

    private final PcService pcService;

    public PcController(PcService pcService) {
        this.pcService = pcService;
    }

    /**
     * Get all computers
     * @param pageable {@link Pageable} with page params
     * @return a {@link Page} of computer entities
     */
    @GetMapping
    public ResponseEntity<Page<PcResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(pcService.getAll(pageable));
    }

    /**
     * Get all computers by computer room
     * @param computerRoomId computer room id
     * @param pageable {@link Pageable} with page params
     * @return a {@link Page} of computer entities
     */
    @GetMapping(params = "computerRoomId")
    public ResponseEntity<Page<PcResponseDto>> getByRoom(
            @RequestParam(name = "computerRoomId") Long computerRoomId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(pcService.getByRoom(computerRoomId, pageable));
    }

    /**
     * Get one computer by id
     * @param id computer id
     * @return computer entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<PcResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pcService.getById(id));
    }

    /**
     * Get one computer by id with optional unwrapping
     * @param id computer id
     * @param unwrap if true, returns unwrapped computer entity
     * @return computer entity (wrapped or unwrapped)
     */
    @GetMapping(value = "/{id}", params = "unwrap")
    public ResponseEntity<Object> getById(
            @PathVariable Long id,
            @RequestParam(name = "unwrap") boolean unwrap
    ) {
        if (unwrap) {
            return ResponseEntity.ok(pcService.getByIdUnwrapped(id));
        }
        return ResponseEntity.ok(pcService.getById(id));
    }

    /**
     * Add a new computer
     * @param pcRequestDto request dto with computer parameters
     * @return new computer entity
     */
    @PostMapping
    public ResponseEntity<PcResponseDto> addPc(
            @Validated(ValidationGroups.OnCreateSequence.class)
            @RequestBody PcRequestDto pcRequestDto
    ) {
        PcResponseDto saved = pcService.save(pcRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Update computer
     * @param id computer id
     * @param pcRequestDto request dto with computer parameters to update
     * @return updated computer entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<PcResponseDto> updatePc(
            @PathVariable Long id,
            @Validated @RequestBody PcRequestDto pcRequestDto
    ) {
        PcResponseDto updated = pcService.update(id, pcRequestDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete computer
     * @param id computer id
     * @return empty response with status 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePc(@PathVariable Long id) {
        pcService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
