package ang.mois.pc.controller;

import ang.mois.pc.dto.request.PcTypeRequestDto;
import ang.mois.pc.dto.response.PcTypeResponseDto;
import ang.mois.pc.entity.PcType;
import ang.mois.pc.service.PcTypeService;
import ang.mois.pc.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/computerConfig")
public class PcTypeController {
    private final PcTypeService pcTypeService;

    @Autowired
    public PcTypeController(PcTypeService pcTypeService) {
        this.pcTypeService = pcTypeService;
    }

    @GetMapping
    public ResponseEntity<List<PcTypeResponseDto>> getAll() {
        return ResponseEntity.ok(pcTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PcTypeResponseDto> getType(@PathVariable Long id) {
        return ResponseEntity.ok(pcTypeService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PcTypeResponseDto> addType(@Validated(ValidationGroups.OnCreateSequence.class) @RequestBody PcTypeRequestDto type) {
        PcTypeResponseDto saved = pcTypeService.save(type);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        pcTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PcTypeResponseDto> updatePcType(@PathVariable Long id, @Validated @RequestBody PcTypeRequestDto pcType) {
        PcTypeResponseDto updated = pcTypeService.update(id, pcType);
        return ResponseEntity.ok(updated);
    }
}
