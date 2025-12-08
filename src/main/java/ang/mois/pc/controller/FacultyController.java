package ang.mois.pc.controller;

import ang.mois.pc.dto.request.FacultyRequestDto;
import ang.mois.pc.dto.response.FacultyResponseDto;
import ang.mois.pc.service.FacultyService;
import ang.mois.pc.validation.ValidationGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for faculty CRUD operations
 */
@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    /**
     * Get all faculties
     * @param pageable {@link Pageable} with page params
     * @return a {@link Page} of faculty entities
     */
    @GetMapping
    public ResponseEntity<Page<FacultyResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(facultyService.getAll(pageable));
    }

    /**
     * Get one faculty
     * @param id faculty id
     * @return {@link FacultyResponseDto} faculty entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getById(id));
    }

    /**
     * Add new faculty
     * @param facultyRequestDto request dto with faculty parameters
     * @return {@link FacultyResponseDto} new faculty entity
     */
    @PostMapping
    public ResponseEntity<FacultyResponseDto> addFaculty(
            @Validated(ValidationGroups.OnCreateSequence.class) @RequestBody FacultyRequestDto facultyRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facultyService.save(facultyRequestDto));
    }

    /**
     * Update faculty
     * @param id faculty id
     * @param facultyRequestDto request dto with faculty parameters to update
     * @return {@link FacultyResponseDto} updated faculty entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> updateFaculty(
            @PathVariable Long id,
            @Validated @RequestBody FacultyRequestDto facultyRequestDto) {
        return ResponseEntity.ok(facultyService.update(id, facultyRequestDto));
    }

    /**
     * Delete faculty
     * @param id faculty id
     * @return empty response with 204 status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
