package ang.mois.pc.service;

import ang.mois.pc.dto.request.FacultyRequestDto;
import ang.mois.pc.util.TestDataProvider;
import ang.mois.pc.dto.request.RoomRequestDto;
import ang.mois.pc.dto.response.FacultyResponseDto;
import ang.mois.pc.dto.response.RoomResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private FacultyService facultyService;

    private FacultyResponseDto faculty;
    private RoomRequestDto validDto;

    @BeforeEach
    void setUp() {
        // Create and store a faculty to attach to the room
        faculty = facultyService.save(TestDataProvider.getFacultyRequestDto());

        validDto = new RoomRequestDto(
                "Room A",
                faculty.id()
        );
    }

    @Test
    void save() {
        RoomResponseDto room = roomService.save(validDto);
        assertNotNull(room);
        verifyParams(room, validDto);
    }

    @Test
    void getById() {
        RoomResponseDto saved = roomService.save(validDto);
        RoomResponseDto fetched = roomService.getById(saved.id());
        verifyParams(fetched, validDto);
    }

    @Test
    void update() {
        RoomResponseDto saved = roomService.save(validDto);
        FacultyRequestDto faculty = TestDataProvider.getFacultyRequestDto();
        FacultyResponseDto newFaculty = facultyService.save(faculty);

        RoomRequestDto updateDto = new RoomRequestDto(
                "Updated Room",
                newFaculty.id()
        );

        RoomResponseDto updated = roomService.update(saved.id(), updateDto);

        RoomRequestDto expected = new RoomRequestDto(
                "Updated Room",
                newFaculty.id()
        );
        verifyParams(updated, expected);
    }

    @Test
    void getByFaculty() {
        roomService.save(validDto);
        List<RoomResponseDto> rooms = roomService.getByFaculty(faculty.id());
        assertFalse(rooms.isEmpty());
        assertEquals(faculty.id(), rooms.getFirst().facultyId());
    }

    @Test
    void delete() {
        RoomResponseDto saved = roomService.save(validDto);
        Long id = saved.id();
        roomService.delete(id);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> roomService.getById(id));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    private void verifyParams(RoomResponseDto room, RoomRequestDto dto) {
        assertEquals(dto.name(), room.name());
        assertEquals(dto.facultyId(), room.facultyId());
        assertEquals(LocalDate.now(), room.createdAt().toLocalDate());
    }
}
