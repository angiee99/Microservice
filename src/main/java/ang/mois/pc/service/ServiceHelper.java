package ang.mois.pc.service;

import ang.mois.pc.entity.Faculty;
import ang.mois.pc.entity.Pc;
import ang.mois.pc.entity.PcType;
import ang.mois.pc.entity.Room;
import ang.mois.pc.repository.FacultyRepository;
import ang.mois.pc.repository.PcRepository;
import ang.mois.pc.repository.PcTypeRepository;
import ang.mois.pc.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceHelper {
    private final FacultyRepository facultyRepository;
    private final RoomRepository roomRepository;
    private final PcRepository pcRepository;
    private final PcTypeRepository pcTypeRepository;

    public ServiceHelper(FacultyRepository facultyRepository, RoomRepository roomRepository, PcRepository pcRepository, PcTypeRepository pcTypeRepository) {
        this.facultyRepository = facultyRepository;
        this.roomRepository = roomRepository;
        this.pcRepository = pcRepository;
        this.pcTypeRepository = pcTypeRepository;
    }

    /**
     * Get faculty by id
     * @param id faculty id
     * @return {@link Faculty} entity if successful, else {@link EntityNotFoundException}
     */
    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Faculty with id " + id + " does not exist"));
    }

    /**
     * Get room by id
     * @param roomId room id
     * @return {@link Room} entity if successful, else {@link EntityNotFoundException}
     */
    public Room getRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()){
            throw new EntityNotFoundException("Room with id " + roomId + " does not exist");
        }
        return room.get();
    }

    /**
     * Get pc type by id
     * @param typeId type id
     * @return {@link PcType} entity if successful, else {@link EntityNotFoundException}
     */
    public PcType getType(Long typeId) {
        Optional<PcType> type = pcTypeRepository.findById(typeId);
        if(type.isEmpty()){
            throw new EntityNotFoundException("PcType with id " + typeId + " does not exist");
        }
        return type.get();
    }

    /**
     * Get PC by id
     * @param id PC id
     * @return {@link Pc} entity if successful, else {@link EntityNotFoundException}
     */
    public Pc getPc(Long id) {
        return pcRepository.findById(id)
                .orElseThrow(() ->new EntityNotFoundException("Pc with id " + id + " does not exist"));
    }
}
