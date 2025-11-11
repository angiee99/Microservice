package ang.mois.pc.repository;

import ang.mois.pc.entity.Faculty;
import ang.mois.pc.entity.Room;
import ang.mois.pc.util.TestDataProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FacultyRepositoryTest {
    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void testSaveAndFindById() {
        Faculty faculty = TestDataProvider.getFaculty("Faculty of Science", "FS");
        Faculty saved = facultyRepository.save(faculty);

        Optional<Faculty> found = facultyRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Faculty of Science");
        assertThat(found.get().getShortcut()).isEqualTo("FS");
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void testUpdateFaculty() {
        Faculty faculty = TestDataProvider.getFaculty("Old Name", "Same");
        Faculty saved = facultyRepository.save(faculty);

        saved.setName("New Name");
        facultyRepository.save(saved);

        Faculty updated = facultyRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getShortcut()).isEqualTo("Same");
    }

    @Test
    void testDeleteFaculty() {
        Faculty faculty = TestDataProvider.getFaculty("To Delete", "TD");
        Faculty saved = facultyRepository.save(faculty);
        assertThat(facultyRepository.findById(saved.getId())).isPresent();

        facultyRepository.deleteById(saved.getId());
        Optional<Faculty> deleted = facultyRepository.findById(saved.getId());

        assertThat(deleted).isEmpty();
    }

    @Test
    @Transactional
    void testFacultyWithRooms() {
        Faculty faculty = TestDataProvider.getFaculty("Faculty of IT", "FIT");
        Room room1 = new Room("R101", faculty);
        Room room2 = new Room("R102", faculty);

        faculty.setRooms(List.of(room1, room2));

        Faculty saved = facultyRepository.save(faculty);

        Optional<Faculty> found = facultyRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getRooms()).hasSize(2);
        assertThat(found.get().getRooms().getFirst().getFaculty()).isEqualTo(found.get());
    }

    @Test
    void testCreatedAtIsSet() {
        Faculty faculty = TestDataProvider.getFaculty("Auto Date", "AD");
        Faculty saved = facultyRepository.save(faculty);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

}

