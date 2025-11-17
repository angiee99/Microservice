package ang.mois.pc.controller;

import ang.mois.pc.dto.request.FacultyRequestDto;
import ang.mois.pc.dto.response.FacultyResponseDto;
import ang.mois.pc.service.FacultyService;
import ang.mois.pc.validation.ValidationGroups;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<Page<FacultyResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        return ResponseEntity.ok(facultyService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getById(id));
    }

    @PostMapping
    public ResponseEntity<FacultyResponseDto> addFaculty(@Validated(ValidationGroups.OnCreateSequence.class) @RequestBody FacultyRequestDto facultyRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facultyService.save(facultyRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> updateFaculty(@PathVariable Long id, @Validated @RequestBody FacultyRequestDto facultyRequestDto) {
        return ResponseEntity.ok(facultyService.update(id, facultyRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
