package ang.mois.pc.controller;

import ang.mois.pc.dto.request.RoomRequestDto;
import ang.mois.pc.dto.response.RoomResponseDto;
import ang.mois.pc.service.RoomService;
import ang.mois.pc.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for computer room CRUD operations
 */
@RestController
@RequestMapping("/computerRoom")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Get all computer rooms
     * @param pageable {@link Pageable} with page params
     * @return a {@link Page} of computer room entities
     */
    @GetMapping
    public ResponseEntity<Page<RoomResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(roomService.getAll(pageable));
    }

    /**
     * Get all computer rooms by faculty
     * @param facultyId faculty id
     * @param pageable {@link Pageable} with page params
     * @return a {@link Page} of computer room entities
     */
    @GetMapping(params = "facultyId")
    public ResponseEntity<Page<RoomResponseDto>> getAllByFaculty(
            @RequestParam(name = "facultyId") Long facultyId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(roomService.getByFaculty(facultyId, pageable));
    }

    /**
     * Get one computer room
     * @param id computer room id
     * @return {@link RoomResponseDto}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    /**
     * Add new computer room
     * @param roomRequestDto request dto with computer room parameters
     * @return new {@link RoomResponseDto}
     */
    @PostMapping
    public ResponseEntity<RoomResponseDto> addRoom(
            @Validated(ValidationGroups.OnCreateSequence.class)
            @RequestBody RoomRequestDto roomRequestDto
    ) {
        RoomResponseDto saved = roomService.save(roomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Delete computer room
     * @param id computer room id
     * @return empty response with status 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update computer room
     * @param id computer room id
     * @param roomRequestDto request dto with computer room parameters to update
     * @return updated {@link RoomResponseDto}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(
            @PathVariable Long id,
            @Validated @RequestBody RoomRequestDto roomRequestDto
    ) {
        RoomResponseDto updated = roomService.update(id, roomRequestDto);
        return ResponseEntity.ok(updated);
    }
}

