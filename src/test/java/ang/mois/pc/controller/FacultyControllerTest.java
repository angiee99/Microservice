package ang.mois.pc.controller;

import ang.mois.pc.util.TestDataProvider;
import ang.mois.pc.controller.exception.GlobalExceptionHandler;
import ang.mois.pc.dto.request.FacultyRequestDto;
import ang.mois.pc.dto.response.FacultyResponseDto;
import ang.mois.pc.service.FacultyService;
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

import java.sql.Time;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for FacultyController.
 * We use @WebMvcTest to load only the controller and related web components.
 * We MUST import GlobalExceptionHandler.class so that our
 * MethodArgumentNotValidException is handled correctly and returns a 400.
 */
@WebMvcTest(controllers = {FacultyController.class, GlobalExceptionHandler.class})
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Used to convert DTOs to JSON strings

    @MockitoBean
    private FacultyService facultyService;

    private FacultyRequestDto validDto;
    private FacultyResponseDto responseDto;

    private final String apiPath = "/faculty";

    @BeforeEach
    void setUp() {
        // A helper to create a fully valid DTO for OnCreate
        validDto = TestDataProvider.getFacultyRequestDto();

        // A mock entity to be returned by the service
        responseDto = TestDataProvider.getFacultyResponseDto();
    }

    // POST /faculty tests
    @Test
    void addValidFaculty() throws Exception {
        when(facultyService.save(any(FacultyRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated());

        verify(facultyService, times(1)).save(any(FacultyRequestDto.class));
    }

    @Test
    void addInvalidFaculty_BlankName() throws Exception {
        // Create a DTO that violates an OnCreate rule
        FacultyRequestDto invalidDto = new FacultyRequestDto(
                "", // Blank name
                "FI",
                "email@example.com",
                Time.valueOf("08:00:00"),
                Time.valueOf("20:00:00"),
                5, 90, 180
        );

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is mandatory"));

        // Verify service was never called
        verify(facultyService, never()).save(any(FacultyRequestDto.class));
    }

    @Test
    void addInvalidFaculty_InvalidEmail() throws Exception {
        // Create a DTO that violates an OnCreate rule
        FacultyRequestDto invalidDto = new FacultyRequestDto(
                "Faculty of Informatics",
                "FI",
                "inv@lid.",
                Time.valueOf("08:00:00"),
                Time.valueOf("20:00:00"),
                5, 90, 180
        );

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email must be a valid address"));

        // Verify service was never called
        verify(facultyService, never()).save(any(FacultyRequestDto.class));
    }

    @Test
    void addInvalidFaculty_TimeIsNull() throws Exception {
        // Create a DTO that violates an OnCreate rule
        FacultyRequestDto invalidDto = new FacultyRequestDto(
                "Faculty of Informatics",
                "FI",
                "email@example.com",
                null, // Null start time
                Time.valueOf("20:00:00"),
                5, 90, 180
        );

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reservationTimeStart").value("Reservation start time is mandatory"));

        verify(facultyService, never()).save(any(FacultyRequestDto.class));
    }

    @Test
    void addInvalidFaculty_NegativeCount() throws Exception {
        // Create a DTO that violates an OnCreate rule
        FacultyRequestDto invalidDto = new FacultyRequestDto(
                "Faculty of Informatics",
                "FI",
                "email@example.com",
                Time.valueOf("8:00:00"),
                Time.valueOf("20:00:00"),
                -5,
                90,
                180
        );

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.maxUserReservationCount").value("Reservation count must be zero or positive"));

        verify(facultyService, never()).save(any(FacultyRequestDto.class));
    }

    //  PUT /faculty/{id} (Default Validation) Test
    @Test
    void updateFaculty() throws Exception {
        // This DTO would FAIL OnCreate validation but should PASS Default validation
        FacultyRequestDto partialDto = new FacultyRequestDto(
                "Updated Faculty Name",
                null, // This is allowed on update
                null, // This is allowed on update
                null,
                null,
                null,
                null,
                null
        );

        when(facultyService.update(eq(1L), any(FacultyRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put(apiPath.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialDto)))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).update(eq(1L), any(FacultyRequestDto.class));
    }

    @Test
    void updateFacultyInvalid_NameIsTooShort() throws Exception {
        FacultyRequestDto invalidPartialDto = new FacultyRequestDto(
                "A", // Too short
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(put(apiPath.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPartialDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name must be between 2 and 100 characters"));

        verify(facultyService, never()).update(anyLong(), any(FacultyRequestDto.class));
    }

    @Test
    void updateFacultyInvalid_InvalidEmail() throws Exception {
        FacultyRequestDto invalidPartialDto = new FacultyRequestDto(
                null,
                null,
                "inv@lid",
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(put(apiPath.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPartialDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email must be a valid address"));

        verify(facultyService, never()).update(anyLong(), any(FacultyRequestDto.class));
    }

    @Test
    void updateFacultyInvalid_ReservationCountIsNegative() throws Exception {
        // This DTO violates a Default rule (@Min(value=0))
        FacultyRequestDto invalidPartialDto = new FacultyRequestDto(
                "A valid name",
                null,
                null,
                null,
                null,
                -1, // Invalid
                null,
                null
        );

        mockMvc.perform(put(apiPath.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPartialDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.maxUserReservationCount").value("Reservation count must be zero or positive"));

        verify(facultyService, never()).update(anyLong(), any(FacultyRequestDto.class));
    }

    // GET /faculty tests
    @Test
    void getAllFaculties() throws Exception {
        when(facultyService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(facultyService).getAll(captor.capture());
    }

    @Test
    void getFacultyById() throws Exception {
        when(facultyService.getById(1L)).thenReturn(responseDto);

        mockMvc.perform(get(apiPath.concat("/1")))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).getById(1L);
    }

    // DELETE /faculty/{id} tests
    @Test
    void deleteFaculty() throws Exception {
        doNothing().when(facultyService).delete(1L);

        mockMvc.perform(delete(apiPath.concat("/1")))
                .andExpect(status().isNoContent());

        verify(facultyService, times(1)).delete(1L);
    }
}

