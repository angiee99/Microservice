package ang.mois.pc.service;

import ang.mois.pc.client.ReservationsClient;
import ang.mois.pc.dto.request.PcRequestDto;
import ang.mois.pc.dto.response.PcResponseDto;
import ang.mois.pc.dto.response.PcUnwrappedResponseDto;
import ang.mois.pc.entity.Pc;
import ang.mois.pc.entity.PcType;
import ang.mois.pc.entity.Room;
import ang.mois.pc.mapper.PcMapper;
import ang.mois.pc.repository.PcRepository;
import ang.mois.pc.repository.PcTypeRepository;
import ang.mois.pc.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Service for PC operations
 */
@Service
public class PcService {
    private final PcRepository pcRepository;
    private final RoomRepository roomRepository;
    private final PcTypeRepository pcTypeRepository;
    private final PcMapper pcMapper;
    private final ReservationsClient reservationsClient;

    @Autowired
    public PcService(PcRepository pcRepository, RoomRepository roomRepository, PcTypeRepository pcTypeRepository, PcMapper pcMapper, ReservationsClient reservationsClient) {
        this.pcRepository = pcRepository;
        this.roomRepository = roomRepository;
        this.pcTypeRepository = pcTypeRepository;
        this.pcMapper = pcMapper;
        this.reservationsClient = reservationsClient;
    }

    /**
     * Get all PCs without pagination
     * @return list of {@link PcResponseDto}
     */
    public List<PcResponseDto> getAll() {
        return pcMapper.toResponseDtoList(pcRepository.findAll());
    }

    /**
     * Get all PCs with pagination support
     * @param pageable {@link Pageable} with page parameters
     * @return a {@link Page} of {@link PcResponseDto}
     */
    public Page<PcResponseDto> getAll(Pageable pageable) {
        Page<Pc> pcPage = pcRepository.findAll(pageable);
        return pcPage.map(pcMapper::toResponseDto);
    }

    /**
     * Get all PCs by room without pagination
     * @param roomId ID of the room
     * @return list of {@link PcResponseDto} in the room
     */
    public List<PcResponseDto> getByRoom(Long roomId) {
        return pcMapper.toResponseDtoList(pcRepository.findByRoomId(roomId));
    }

    /**
     * Get all PCs by room with pagination
     * @param roomId ID of the room
     * @param pageable {@link Pageable} with page parameters
     * @return a {@link Page} of {@link PcResponseDto} in the room
     */
    public Page<PcResponseDto> getByRoom(Long roomId, Pageable pageable) {
        Page<Pc> pcPage = pcRepository.findByRoomId(roomId, pageable);
        return pcPage.map(pcMapper::toResponseDto);
    }

    /**
     * Get a PC by ID
     * @param id PC ID
     * @return {@link PcResponseDto}
     */
    public PcResponseDto getById(Long id) {
        Pc pc = getPc(id);
        return pcMapper.toResponseDto(pc);
    }

    /**
     * Get a PC by ID and return an unwrapped response DTO
     * @param id PC ID
     * @return {@link PcUnwrappedResponseDto}
     */
    public PcUnwrappedResponseDto getByIdUnwrapped(Long id) {
        Pc pc = getPc(id);
        return pcMapper.toUnwrappedResponseDto(pc);
    }

    /**
     * Create a new PC and save the entity
     * @param pcRequestDto request DTO with PC parameters
     * @return saved {@link PcResponseDto}
     */
    public PcResponseDto save(PcRequestDto pcRequestDto) {
        // retrieve foreign key entities and verify relation
        Room room = getRoom(pcRequestDto.computerRoomId());
        PcType type = getType(pcRequestDto.configId());

        // map basic properties
        Pc pc = pcMapper.toEntity(pcRequestDto);

        // set FK entities
        pc.setRoom(room);
        pc.setPcType(type);

        return pcMapper.toResponseDto(pcRepository.save(pc));
    }

    /**
     * Update an existing PC
     * @param id PC ID
     * @param updatePcRequestDto request DTO with updated PC parameters
     * @return updated {@link PcResponseDto}
     */
    public PcResponseDto update(Long id, PcRequestDto updatePcRequestDto) {
        Pc pc = getPc(id);
        // verify foreign key relations
        if(updatePcRequestDto.computerRoomId() != null) {
            Room room = getRoom(updatePcRequestDto.computerRoomId());
            pc.setRoom(room);
        }

        if(updatePcRequestDto.configId() != null) {
            PcType type = getType(updatePcRequestDto.configId());
            pc.setPcType(type);
        }
        // merge entities
        pcMapper.updateEntityFromDto(updatePcRequestDto, pc);

        return pcMapper.toResponseDto(pcRepository.save(pc));
    }

    private Room getRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()){
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist");
        }
        return room.get();
    }

    private PcType getType(Long typeId) {
        Optional<PcType> type = pcTypeRepository.findById(typeId);
        if(type.isEmpty()){
            throw new IllegalArgumentException("PcType with id " + typeId + " does not exist");
        }
        return type.get();
    }

    /**
     * Delete a PC
     * @param id PC ID
     * @throws FKConflictException if there are active reservations for the PC
     */
    public void delete(Long id) {
        Pc pc = getPc(id);

        // Check reservations via another microservice
        boolean hasReservations = reservationsClient.hasReservationsForPc(pc.getId());

        if (hasReservations) {
            throw new FKConflictException(
                    "Cannot delete PC " + id + " as there are active reservations for this PC."
            );
        }
        pcRepository.deleteById(id);
    }

    /**
     * Get PCs by type
     * @param type PC Type entity
     * @return list of {@link PcResponseDto} of that type
     */
    public List<PcResponseDto> getByType(PcType type) {
        return pcMapper.toResponseDtoList(pcRepository.findByPcType(type));
    }

    private Pc getPc(Long id) {
        return pcRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Pc with id " + id + " does not exist"));
    }
}