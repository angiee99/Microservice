package ang.mois.pc.controller;

import ang.mois.pc.dto.request.RoomRequestDto;
import ang.mois.pc.dto.response.RoomResponseDto;
import ang.mois.pc.service.RoomService;
import ang.mois.pc.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/computerRoom")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAll() {
        return ResponseEntity.ok(roomService.getAll());
    }

    @GetMapping(params="facultyId")
    public ResponseEntity<List<RoomResponseDto>> getAll(@RequestParam(name = "facultyId") Long facultyId) {
        return ResponseEntity.ok(roomService.getByFaculty(facultyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RoomResponseDto> addRoom(@Validated(ValidationGroups.OnCreateSequence.class) @RequestBody RoomRequestDto roomRequestDto) {
        RoomResponseDto saved = roomService.save(roomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long id, @Validated @RequestBody RoomRequestDto roomRequestDto) {
        RoomResponseDto updated = roomService.update(id, roomRequestDto);
        return ResponseEntity.ok(updated);
    }
}
