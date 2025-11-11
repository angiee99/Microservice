package ang.mois.pc.repository;

import ang.mois.pc.entity.*;
import ang.mois.pc.util.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private PcTypeRepository pcTypeRepository;

    @Test
    void testSaveAndFindById() {
        Faculty faculty = TestDataProvider.getFaculty("Faculty of Science", "FS");
        facultyRepository.save(faculty);

        Room room = new Room("A101", faculty);
        Room saved = roomRepository.save(room);

        Optional<Room> found = roomRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("A101");
        assertThat(found.get().getFaculty().getName()).isEqualTo("Faculty of Science");
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void testFindByFaculty() {
        Faculty f1 = TestDataProvider.getFaculty("Faculty of IT", "FIT");
        Faculty f2 = TestDataProvider.getFaculty("Faculty of Law", "FLAW");
        facultyRepository.saveAll(List.of(f1, f2));

        Room r1 = new Room("R101", f1);
        Room r2 = new Room("R102", f1);
        Room r3 = new Room("R201", f2);
        roomRepository.saveAll(List.of(r1, r2, r3));

        List<Room> fitRooms = roomRepository.findByFacultyId(f1.getId());
        List<Room> lawRooms = roomRepository.findByFacultyId(f2.getId());

        assertThat(fitRooms).hasSize(2);
        assertThat(lawRooms).hasSize(1);
        assertThat(fitRooms).extracting(Room::getName).containsExactlyInAnyOrder("R101", "R102");
        assertThat(lawRooms.getFirst().getName()).isEqualTo("R201");
    }

    @Test
    @Transactional
    void testCascadeSaveWithPcs() {
        Faculty faculty = TestDataProvider.getFaculty("Faculty of Engineering", "FE");
        facultyRepository.save(faculty);

        PcType pcType = new PcType("Office", "i5", "16GB", "GTX 1050");
        pcTypeRepository.save(pcType);

        Pc pc1 = new Pc("ok", true, null, pcType);
        Pc pc2 = new Pc("broken", true, null, pcType);
        pcType.setPcs(List.of(pc1, pc2));

        Room room = new Room("B202", faculty);
        pc1.setRoom(room);
        pc2.setRoom(room);
        room.setPcs(List.of(pc1, pc2));
        faculty.setRooms(List.of(room));

        Room saved = roomRepository.save(room);

        Room found = roomRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getPcs()).hasSize(2);
        assertThat(found.getPcs().getFirst().getRoom()).isEqualTo(found);
    }

}
