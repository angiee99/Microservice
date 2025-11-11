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
class PcRepositoryTest {

    @Autowired
    private PcRepository pcRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private PcTypeRepository pcTypeRepository;

    @Test
    void testSaveAndFindById() {
        Faculty faculty = facultyRepository.save(TestDataProvider.getFaculty("Faculty of IT", "FIT"));
        Room room = roomRepository.save(new Room("A101", faculty));
        PcType type = pcTypeRepository.save(new PcType("Office", "i5", "16GB", "GTX 1060"));

        Pc pc = new Pc("ok", true, room, type);
        Pc saved = pcRepository.save(pc);

        Optional<Pc> found = pcRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().isAvailable()).isTrue();
        assertThat(found.get().getRoom().getName()).isEqualTo("A101");
        assertThat(found.get().getPcType().getCpu()).isEqualTo("i5");
    }

    @Test
    void testFindByRoom() {
        Faculty faculty = facultyRepository.save(TestDataProvider.getFaculty("Faculty of Engineering", "FE"));
        Room room1 = roomRepository.save(new Room("C201", faculty));
        Room room2 = roomRepository.save(new Room("C202", faculty));
        PcType type = pcTypeRepository.save(new PcType("Workstation", "i7", "32GB", "RTX 3070"));

        Pc pc1 = pcRepository.save(new Pc("ok", true, room1, type));
        Pc pc2 = pcRepository.save(new Pc("broken", false, room2, type));

        List<Pc> pcsRoom1 = pcRepository.findByRoomId(room1.getId());
        List<Pc> pcsRoom2 = pcRepository.findByRoomId(room2.getId());

        assertThat(pcsRoom1).containsExactly(pc1);
        assertThat(pcsRoom2).containsExactly(pc2);
    }

    @Test
    void testFindAllByRoomIn() {
        Faculty faculty = facultyRepository.save(TestDataProvider.getFaculty("Faculty of Design", "FD"));
        Room r1 = roomRepository.save(new Room("D101", faculty));
        Room r2 = roomRepository.save(new Room("D102", faculty));
        Room r3 = roomRepository.save(new Room("D103", faculty));
        PcType type = pcTypeRepository.save(new PcType("Design", "M1", "16GB", "Integrated"));

        Pc pc1 = pcRepository.save(new Pc("ok", true, r1, type));
        Pc pc2 = pcRepository.save(new Pc("ok", true, r2, type));
        pcRepository.save(new Pc("ok", true, r3, type)); // not included

        List<Pc> found = pcRepository.findAllByRoomIn(List.of(r1, r2));

        assertThat(found).containsExactlyInAnyOrder(pc1, pc2);
    }

    @Test
    @Transactional
    void testFindByPcType() {
        Faculty faculty = facultyRepository.save(TestDataProvider.getFaculty("Faculty of Business", "FB"));
        Room room = roomRepository.save(new Room("E101", faculty));
        PcType officeType = pcTypeRepository.save(new PcType("Office", "i5", "8GB", "Integrated"));
        PcType gamingType = pcTypeRepository.save(new PcType("Gaming", "Ryzen 7", "16GB", "RTX 2060"));

        Pc pc1 = pcRepository.save(new Pc("ok", true, room, officeType));
        Pc pc2 = pcRepository.save(new Pc("ok", true, room, gamingType));

        List<Pc> officePcs = pcRepository.findByPcType(officeType);
        List<Pc> gamingPcs = pcRepository.findByPcType(gamingType);

        assertThat(officePcs).containsExactly(pc1);
        assertThat(gamingPcs).containsExactly(pc2);
    }
}