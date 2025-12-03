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


@RestController
@RequestMapping("/computer")
public class PcController {
    private final PcService pcService;

    public PcController(PcService pcService) {
        this.pcService = pcService;
    }

    @GetMapping
    public ResponseEntity<Page<PcResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(pcService.getAll(pageable));
    }

    @GetMapping(params="computerRoomId")
    public ResponseEntity<Page<PcResponseDto>> getByRoom(
            @RequestParam(name="computerRoomId") Long computerRoomId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(pcService.getByRoom(computerRoomId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PcResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pcService.getById(id));
    }

    @GetMapping(value="/{id}", params = "unwrap")
    public ResponseEntity<Object> getById(@PathVariable Long id, @RequestParam(name="unwrap") boolean unwrap) {
        if(unwrap) {
            return ResponseEntity.ok(pcService.getByIdUnwrapped(id));
        }
        return ResponseEntity.ok(pcService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PcResponseDto> addPc(@Validated(ValidationGroups.OnCreateSequence.class) @RequestBody PcRequestDto pcRequestDto) {
        PcResponseDto saved = pcService.save(pcRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PcResponseDto> updatePc(@PathVariable Long id, @Validated @RequestBody PcRequestDto pcRequestDto) {
        PcResponseDto updated = pcService.update(id, pcRequestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePc(@PathVariable Long id) {
        pcService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
