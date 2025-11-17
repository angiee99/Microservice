package ang.mois.pc.repository;

import ang.mois.pc.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByFacultyId(Long facultyId);

    Page<Room> findByFacultyId(Long facultyId, Pageable pageable);

    boolean existsByFacultyId(Long facultyId);
}
