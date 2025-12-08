package ang.mois.pc.controller;

import ang.mois.pc.dto.request.PcTypeRequestDto;
import ang.mois.pc.dto.response.PcTypeResponseDto;
import ang.mois.pc.service.PcTypeService;
import ang.mois.pc.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for computer configuration (PC type) CRUD operations
 */
@RestController
@RequestMapping("/computerConfig")
public class PcTypeController {

    private final PcTypeService pcTypeService;

    @Autowired
    public PcTypeController(PcTypeService pcTypeService) {
        this.pcTypeService = pcTypeService;
    }

    /**
     * Get all computer configurations
     * @param pageable {@link Pageable} with page params
     * @return a {@link Page} of computer configuration entities
     */
    @GetMapping
    public ResponseEntity<Page<PcTypeResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(pcTypeService.getAll(pageable));
    }

    /**
     * Get one computer configuration
     * @param id computer configuration id
     * @return computer configuration entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<PcTypeResponseDto> getType(@PathVariable Long id) {
        return ResponseEntity.ok(pcTypeService.getById(id));
    }

    /**
     * Add new computer configuration
     * @param type request dto with computer configuration parameters
     * @return new computer configuration entity
     */
    @PostMapping
    public ResponseEntity<PcTypeResponseDto> addType(
            @Validated(ValidationGroups.OnCreateSequence.class)
            @RequestBody PcTypeRequestDto type
    ) {
        PcTypeResponseDto saved = pcTypeService.save(type);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Delete computer configuration
     * @param id computer configuration id
     * @return empty response with 204 status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        pcTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update computer configuration
     * @param id computer configuration id
     * @param pcType request dto with computer configuration parameters to update
     * @return updated computer configuration entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<PcTypeResponseDto> updatePcType(
            @PathVariable Long id,
            @Validated @RequestBody PcTypeRequestDto pcType
    ) {
        PcTypeResponseDto updated = pcTypeService.update(id, pcType);
        return ResponseEntity.ok(updated);
    }
}

