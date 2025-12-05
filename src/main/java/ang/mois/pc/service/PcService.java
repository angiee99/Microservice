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

    public List<PcResponseDto> getAll() {
        return pcMapper.toResponseDtoList(pcRepository.findAll());
    }

    public Page<PcResponseDto> getAll(Pageable pageable) {
        Page<Pc> pcPage = pcRepository.findAll(pageable);
        return pcPage.map(pcMapper::toResponseDto);
    }

    public List<PcResponseDto> getByRoom(Long roomId) {
        return pcMapper.toResponseDtoList(pcRepository.findByRoomId(roomId));
    }

    public Page<PcResponseDto> getByRoom(Long roomId, Pageable pageable) {
        Page<Pc> pcPage = pcRepository.findByRoomId(roomId, pageable);
        return pcPage.map(pcMapper::toResponseDto);
    }

    public PcResponseDto getById(Long id) {
        Pc pc = getPc(id);
        return pcMapper.toResponseDto(pc);
    }

    public PcUnwrappedResponseDto getByIdUnwrapped(Long id) {
        Pc pc = getPc(id);
        return pcMapper.toUnwrappedResponseDto(pc);
    }

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

    public void delete(Long id) {
        Pc pc = getPc(id);
        // Check reservations via gateway
        boolean hasReservations = reservationsClient.hasReservationsForPc(pc.getId());

        if (hasReservations) {
            throw new FKConflictException(
                    "Cannot delete PC " + id + " as there are active reservations for this pc."
            );
        }
        pcRepository.deleteById(id);
    }

    public List<PcResponseDto> getByType(PcType type) {
        return pcMapper.toResponseDtoList(pcRepository.findByPcType(type));
    }

    private Pc getPc(Long id) {
        return pcRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Pc with id " + id + " does not exist"));
    }
}