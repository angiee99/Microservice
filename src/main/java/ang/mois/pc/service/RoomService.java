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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<RoomResponseDto> getAll() {
        return roomMapper.toResponseDtoList(roomRepository.findAll());
    }

    public Page<RoomResponseDto> getAll(Pageable pageable) {
        Page<Room> roomPage = roomRepository.findAll(pageable);
        return roomPage.map(roomMapper::toResponseDto);
    }

    public RoomResponseDto getById(Long id) {
        Room room = getRoom(id);
        return roomMapper.toResponseDto(room);
    }

    public List<RoomResponseDto> getByFaculty(Long facultyId) {
        return roomMapper.toResponseDtoList(roomRepository.findByFacultyId(facultyId));
    }

    public Page<RoomResponseDto> getByFaculty(Long facultyId, Pageable pageable) {
        Page<Room> roomPage = roomRepository.findByFacultyId(facultyId, pageable);
        return roomPage.map(roomMapper::toResponseDto);
    }

    public RoomResponseDto save(RoomRequestDto createRoomRequestDto) {
        Faculty faculty = getFaculty(createRoomRequestDto.facultyId());

        Room room = roomMapper.toEntity(createRoomRequestDto);
        room.setFaculty(faculty);

        return roomMapper.toResponseDto(roomRepository.save(room));
    }

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