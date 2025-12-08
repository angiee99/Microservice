package ang.mois.pc.service;

import ang.mois.pc.dto.request.RoomRequestDto;
import ang.mois.pc.dto.response.RoomResponseDto;
import ang.mois.pc.entity.Faculty;
import ang.mois.pc.entity.Room;
import ang.mois.pc.mapper.RoomMapper;
import ang.mois.pc.repository.FacultyRepository;
import ang.mois.pc.repository.PcRepository;
import ang.mois.pc.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service for Room operations
 */
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final FacultyRepository facultyRepository;
    private final PcRepository pcRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, FacultyRepository facultyRepository, PcRepository pcRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.facultyRepository = facultyRepository;
        this.pcRepository = pcRepository;
        this.roomMapper = roomMapper;
    }

    /**
     * Get all rooms without pagination.
     * @return list of {@link RoomResponseDto}
     */
    public List<RoomResponseDto> getAll() {
        return roomMapper.toResponseDtoList(roomRepository.findAll());
    }

    /**
     * Get all rooms with pagination support.
     * @param pageable {@link Pageable} with page parameters
     * @return a {@link Page} of {@link RoomResponseDto}
     */
    public Page<RoomResponseDto> getAll(Pageable pageable) {
        Page<Room> roomPage = roomRepository.findAll(pageable);
        return roomPage.map(roomMapper::toResponseDto);
    }

    /**
     * Get a room by ID.
     * @param id Room ID
     * @return {@link RoomResponseDto}
     */
    public RoomResponseDto getById(Long id) {
        Room room = getRoom(id);
        return roomMapper.toResponseDto(room);
    }

    /**
     * Get all rooms for a specific faculty without pagination.
     * @param facultyId Faculty ID
     * @return list of {@link RoomResponseDto} associated with the faculty
     */
    public List<RoomResponseDto> getByFaculty(Long facultyId) {
        return roomMapper.toResponseDtoList(roomRepository.findByFacultyId(facultyId));
    }

    /**
     * Get all rooms for a specific faculty with pagination support.
     * @param facultyId Faculty ID
     * @param pageable {@link Pageable} with page parameters
     * @return a {@link Page} of {@link RoomResponseDto} with the faculty
     */
    public Page<RoomResponseDto> getByFaculty(Long facultyId, Pageable pageable) {
        Page<Room> roomPage = roomRepository.findByFacultyId(facultyId, pageable);
        return roomPage.map(roomMapper::toResponseDto);
    }

    /**
     * Create a new room
     * @param createRoomRequestDto request DTO with room parameters
     * @return saved {@link RoomResponseDto}
     */
    public RoomResponseDto save(RoomRequestDto createRoomRequestDto) {
        Faculty faculty = getFaculty(createRoomRequestDto.facultyId());

        Room room = roomMapper.toEntity(createRoomRequestDto);
        room.setFaculty(faculty);

        return roomMapper.toResponseDto(roomRepository.save(room));
    }

    /**
     * Delete a room by ID.
     * Checks that no PCs reference this room before deletion.
     * @param id Room ID
     * @throws FKConflictException if there are PCs still associated with the room
     */
    public void delete(Long id) {
        // verify if room exists
        if(!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("Room with id " + id + " does not exist");
        }

        if(pcRepository.existsByRoomId(id)) {
            throw new FKConflictException(
                    "Cannot delete room with id: " + id + ", because there are still computers associated with it."
            );
        }

        // delete only if no computer references this room
        roomRepository.deleteById(id);
    }

    /**
     * Update an existing room.
     * @param idR Room ID
     * @param roomRequestDto request DTO with updated room parameters
     * @return updated {@link RoomResponseDto}
     */
    public RoomResponseDto update(Long idR, RoomRequestDto roomRequestDto) {
        Room room = getRoom(idR);

        if(roomRequestDto.facultyId() != null) {
            Faculty faculty = getFaculty(roomRequestDto.facultyId());
            room.setFaculty(faculty);
        }

        // map by copying non null values from the update dto
        roomMapper.updateEntityFromDto(roomRequestDto, room);

        return roomMapper.toResponseDto(roomRepository.save(room));
    }

    private Room getRoom(Long id) {
         return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room with id " + id + " does not exist"));
    }

    private Faculty getFaculty(Long facultyId) {
        return facultyRepository.findById(facultyId).orElseThrow(
                ()-> new IllegalArgumentException("Faculty with id " + facultyId + " does not exist"));
    }
}