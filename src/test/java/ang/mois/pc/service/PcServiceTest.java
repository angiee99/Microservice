package ang.mois.pc.service;

import ang.mois.pc.client.ReservationsClient;
import ang.mois.pc.dto.request.PcRequestDto;
import ang.mois.pc.dto.response.PcResponseDto;
import ang.mois.pc.entity.Faculty;
import ang.mois.pc.entity.PcType;
import ang.mois.pc.entity.Room;
import ang.mois.pc.repository.FacultyRepository;
import ang.mois.pc.repository.PcRepository;
import ang.mois.pc.repository.RoomRepository;
import ang.mois.pc.repository.PcTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class PcServiceTest {

    @Autowired
    private PcService pcService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private PcTypeRepository pcTypeRepository;

    @Autowired
    private PcRepository pcRepository;

    @MockitoBean
    private ReservationsClient reservationsClient;

    private PcRequestDto validDto;
    private Room savedRoom;
    private Faculty savedFaculty;
    private PcType savedType;

    @BeforeEach
    void setUp() {
        // prepare related entities
        savedFaculty = new Faculty();
        facultyRepository.save(savedFaculty);

        savedRoom = new Room();
        savedRoom.setName("Main Room");
        savedRoom.setFaculty(savedFaculty);
        savedRoom = roomRepository.save(savedRoom);

        savedType = new PcType();
        savedType.setName("Gaming Rig");
        savedType.setCpu("Ryzen 9");
        savedType.setRam("32GB");
        savedType.setGpu("RTX 4080");
        savedType = pcTypeRepository.save(savedType);

        validDto = new PcRequestDto(
                "High-End PC",
                Boolean.TRUE,
                savedRoom.getId(),
                savedType.getId()
        );
    }

    @Test
    void save() {
        PcResponseDto pc = pcService.save(validDto);

        assertNotNull(pc);
        assertEquals(validDto.name(), pc.name());
        assertEquals(validDto.available(), pc.available());
        assertEquals(savedRoom.getId(), pc.computerRoomId());
        assertEquals(savedType.getId(), pc.configId());
    }

    @Test
    void getById() {
        PcResponseDto pc = pcService.save(validDto);
        PcResponseDto found = pcService.getById(pc.id());

        assertNotNull(found);
        assertEquals(pc.id(), found.id());
        assertEquals(pc.name(), found.name());
    }

    @Test
    void update() {
        PcResponseDto pc = pcService.save(validDto);

        // prepare a DTO that updates the name and sets availability false
        PcRequestDto updateDto = new PcRequestDto(
                "Updated Name",
                Boolean.FALSE,
                null, // keep old room
                null  // keep old type
        );

        PcResponseDto updated = pcService.update(pc.id(), updateDto);

        assertEquals("Updated Name", updated.name());
        assertFalse(updated.available());
        assertEquals(pc.computerRoomId(), updated.computerRoomId());
        assertEquals(pc.configId(), updated.configId());
    }

    @Test
    void update_changeRelations() {
        PcResponseDto pc = pcService.save(validDto);

        // new relations
        Room newRoom = new Room();
        newRoom.setName("Another Room");
        newRoom.setFaculty(savedFaculty);
        newRoom = roomRepository.save(newRoom);

        PcType newType = new PcType();
        newType.setName("Office PC");
        newType.setCpu("i5");
        newType.setRam("16GB");
        newType.setGpu("GTX 1650");
        newType = pcTypeRepository.save(newType);

        PcRequestDto updateDto = new PcRequestDto(
                "Moved PC",
                Boolean.TRUE,
                newRoom.getId(),
                newType.getId()
        );

        PcResponseDto updated = pcService.update(pc.id(), updateDto);

        assertEquals("Moved PC", updated.name());
        assertEquals(newRoom.getId(), updated.computerRoomId());
        assertEquals(newType.getId(), updated.configId());
    }

    @Test
    void getByRoom() {
        PcResponseDto pc = pcService.save(validDto);
        List<PcResponseDto> pcs = pcService.getByRoom(savedRoom.getId());

        assertFalse(pcs.isEmpty());
        assertTrue(pcs.stream().anyMatch(p -> p.id().equals(pc.id())));
    }

    @Test
    void getByType() {
        PcResponseDto pc = pcService.save(validDto);
        List<PcResponseDto> pcs = pcService.getByType(savedType);

        assertFalse(pcs.isEmpty());
        assertTrue(pcs.stream().anyMatch(p -> p.id().equals(pc.id())));
    }

    @Test
    void deleteHappyDay() {
        // pc exists
        PcResponseDto pc = pcService.save(validDto);
        // gateway says there are NO reservations
        when(reservationsClient.hasReservationsForPc(pc.id())).thenReturn(false);

        // delete
        pcService.delete(pc.id());

        assertFalse(pcRepository.findById(pc.id()).isPresent());
    }

    @Test
    void deleteReservationExists() {
        // pc exists
        PcResponseDto pc = pcService.save(validDto);
        // gateway says reservations exist
        when(reservationsClient.hasReservationsForPc(pc.id())).thenReturn(true);

        // try to delete it, the exception must be thrown though
        assertThrows(FKConflictException.class, () -> pcService.delete(pc.id()));

        assertTrue(pcRepository.findById(pc.id()).isPresent());
    }

    @Test
    void deletePcDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> pcService.delete(192390820L));
    }

    @Test
    void getById_nonexistent_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> pcService.getById(9999L));
    }

    @Test
    void save_invalidRoom_shouldThrow() {
        PcRequestDto dto = new PcRequestDto(
                "Bad PC",
                Boolean.TRUE,
                9999L, // nonexistent room
                savedType.getId()
        );
        assertThrows(IllegalArgumentException.class, () -> pcService.save(dto));
    }

    @Test
    void save_invalidType_shouldThrow() {
        PcRequestDto dto = new PcRequestDto(
                "Bad PC",
                Boolean.TRUE,
                savedRoom.getId(),
                9999L // nonexistent type
        );
        assertThrows(IllegalArgumentException.class, () -> pcService.save(dto));
    }
}
