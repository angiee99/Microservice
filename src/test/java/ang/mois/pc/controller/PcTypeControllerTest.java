package ang.mois.pc.controller;

import ang.mois.pc.util.TestDataProvider;
import ang.mois.pc.controller.exception.GlobalExceptionHandler;
import ang.mois.pc.dto.request.PcTypeRequestDto;
import ang.mois.pc.dto.response.PcTypeResponseDto;
import ang.mois.pc.service.PcTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for PcTypeController.
 * Uses @WebMvcTest to isolate the web layer and validate behavior, validation, and service calls.
 */
@WebMvcTest(controllers = {PcTypeController.class, GlobalExceptionHandler.class})
class PcTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PcTypeService pcTypeService;

    private PcTypeRequestDto requestDto;
    private PcTypeResponseDto responseDto;

    private final String apiPath = "/computerConfig";

    @BeforeEach
    void setUp() {
        requestDto = TestDataProvider.getPcTypeRequestDto();
        responseDto = TestDataProvider.getPcTypeResponseDto();
    }

    // --- GET /computerConfig ---
    @Test
    void getAllPcTypes() throws Exception {
        when(pcTypeService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get(apiPath)).andExpect(status().isOk());

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(pcTypeService).getAll(captor.capture());
    }


    // --- GET /computerConfig/{id} ---
    @Test
    void getPcTypeById() throws Exception {
        when(pcTypeService.getById(1L)).thenReturn(responseDto);

        mockMvc.perform(get(apiPath + "/1"))
                .andExpect(status().isOk());

        verify(pcTypeService, times(1)).getById(1L);
    }

    // --- POST /computerConfig ---
    @Test
    void addValidPcType() throws Exception {
        when(pcTypeService.save(any(PcTypeRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(pcTypeService, times(1)).save(any(PcTypeRequestDto.class));
    }

    @Test
    void addInvalidPcType_BlankName() throws Exception {
        PcTypeRequestDto invalidDto = new PcTypeRequestDto(
                "", // Invalid name
                "Intel i9",
                "32GB",
                "RTX 4090"
        );

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is mandatory"));

        verify(pcTypeService, never()).save(any(PcTypeRequestDto.class));
    }

    @Test
    void addInvalidPcType_BlankCpu() throws Exception {
        PcTypeRequestDto invalidDto = new PcTypeRequestDto(
                "Gaming PC",
                "", // Invalid CPU
                "32GB",
                "RTX 4090"
        );

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cpu").value("Cpu is mandatory"));

        verify(pcTypeService, never()).save(any(PcTypeRequestDto.class));
    }

    // --- PUT /computerConfig/{id} ---
    @Test
    void updatePcType_Valid() throws Exception {
        PcTypeRequestDto updateDto = new PcTypeRequestDto(
                "Updated PC",
                "AMD Ryzen 9",
                "64GB",
                "RTX 4080"
        );

        when(pcTypeService.update(eq(1L), any(PcTypeRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put(apiPath + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        verify(pcTypeService, times(1)).update(eq(1L), any(PcTypeRequestDto.class));
    }

    @Test
    void updatePcType_InvalidNameTooShort() throws Exception {
        PcTypeRequestDto invalidDto = new PcTypeRequestDto(
                "A", // Too short (min=2)
                "Intel i9",
                "32GB",
                "RTX 4090"
        );

        mockMvc.perform(put(apiPath + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name must be between 2 and 100 characters"));

        verify(pcTypeService, never()).update(anyLong(), any(PcTypeRequestDto.class));
    }

    // --- DELETE /computerConfig/{id} ---
    @Test
    void deletePcType() throws Exception {
        doNothing().when(pcTypeService).delete(1L);

        mockMvc.perform(delete(apiPath + "/1"))
                .andExpect(status().isNoContent());

        verify(pcTypeService, times(1)).delete(1L);
    }
}
