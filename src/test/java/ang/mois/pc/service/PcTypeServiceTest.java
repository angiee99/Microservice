package ang.mois.pc.service;

import ang.mois.pc.util.TestDataProvider;
import ang.mois.pc.dto.request.PcTypeRequestDto;
import ang.mois.pc.dto.response.PcTypeResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PcTypeServiceTest {

    @Autowired
    private PcTypeService pcTypeService;

    private PcTypeRequestDto pcTypeRequestDto;

    @BeforeEach
    void setUp() {
        pcTypeRequestDto = TestDataProvider.getPcTypeRequestDto();
    }

    @Test
    void save() {
        PcTypeResponseDto type = pcTypeService.save(pcTypeRequestDto);
        assertNotNull(type);
        verifyParams(type, pcTypeRequestDto);
    }

    @Test
    void getById() {
        PcTypeResponseDto type = pcTypeService.save(pcTypeRequestDto);
        PcTypeResponseDto fetched = pcTypeService.getById(type.id());
        verifyParams(fetched, pcTypeRequestDto);
    }

    @Test
    void update() {
        PcTypeResponseDto type = pcTypeService.save(pcTypeRequestDto);

        PcTypeRequestDto updateDto = new PcTypeRequestDto(
                "Office PC",
                null,
                "8GB",
                null
        );

        PcTypeResponseDto updated = pcTypeService.update(type.id(), updateDto);
        PcTypeRequestDto merged = new PcTypeRequestDto(
                updateDto.name(),
                pcTypeRequestDto.cpu(),
                updated.ram(),
                pcTypeRequestDto.gpu()
        );

        verifyParams(updated, merged);
    }

    @Test
    void getAll() {
        pcTypeService.save(pcTypeRequestDto);
        var types = pcTypeService.getAll();
        assertFalse(types.isEmpty());
    }

    @Test
    void delete() {
        PcTypeResponseDto type = pcTypeService.save(pcTypeRequestDto);
        Long id = type.id();

        pcTypeService.delete(id);

        Exception ex = assertThrows(EntityNotFoundException.class, () -> pcTypeService.getById(id));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    private void verifyParams(PcTypeResponseDto output, PcTypeRequestDto expected) {
        assertEquals(expected.name(), output.name());
        assertEquals(expected.cpu(), output.cpu());
        assertEquals(expected.ram(), output.ram());
        assertEquals(expected.gpu(), output.gpu());
    }
}

