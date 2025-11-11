package ang.mois.pc.repository;

import ang.mois.pc.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PcTypeRepositoryTest {

    @Autowired
    private PcTypeRepository pcTypeRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void testSaveAndFindById() {
        PcType pcType = new PcType("Gaming", "Intel i9", "32GB", "RTX 4090");
        PcType saved = pcTypeRepository.save(pcType);

        Optional<PcType> found = pcTypeRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Gaming");
        assertThat(found.get().getCpu()).isEqualTo("Intel i9");
        assertThat(found.get().getRam()).isEqualTo("32GB");
        assertThat(found.get().getGpu()).isEqualTo("RTX 4090");
    }

    @Test
    void testUpdatePcType() {
        PcType pcType = new PcType("Office", "Intel i5", "16GB", "GTX 1060");
        PcType saved = pcTypeRepository.save(pcType);

        saved.setCpu("Intel i7");
        pcTypeRepository.save(saved);

        PcType updated = pcTypeRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getCpu()).isEqualTo("Intel i7");
    }

    @Test
    @Transactional
    void testCascadeSaveWithPcs() {
        // Create related Room
        Faculty faculty = new Faculty("Faculty of IT", "FIT", "fit@specialdomain.com");
        Room room = new Room("R101", faculty);
        faculty.setRooms(List.of(room));

        facultyRepository.save(faculty);

        // Create PcType and its PCs
        PcType pcType = new PcType("Workstation", "Ryzen 9", "64GB", "RTX 4080");
        Pc pc1 = new Pc("ok", true, room, pcType);
        Pc pc2 = new Pc("ok", true, room, pcType);

        pcType.setPcs(List.of(pc1, pc2));

        PcType saved = pcTypeRepository.save(pcType);

        PcType found = pcTypeRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getPcs()).hasSize(2);
        assertThat(found.getPcs().getFirst().getPcType()).isEqualTo(found);
        assertThat(found.getPcs().getFirst().getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}
