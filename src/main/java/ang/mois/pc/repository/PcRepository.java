package ang.mois.pc.repository;

import ang.mois.pc.entity.Pc;
import ang.mois.pc.entity.PcType;
import ang.mois.pc.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PcRepository extends JpaRepository<Pc, Long> {
    List<Pc> findByRoomId(Long roomId);

    Page<Pc> findByRoomId(Long roomId, Pageable pageable);

    List<Pc> findAllByRoomIn(List<Room> room);

    List<Pc> findByPcType(PcType type);

    boolean existsByPcTypeId(Long typeId);

    boolean existsByRoomId(Long roomId);
}
