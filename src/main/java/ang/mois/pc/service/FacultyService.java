package ang.mois.pc.service;

import ang.mois.pc.dto.request.FacultyRequestDto;
import ang.mois.pc.dto.response.FacultyResponseDto;
import ang.mois.pc.entity.Faculty;
import ang.mois.pc.mapper.FacultyMapper;
import ang.mois.pc.repository.FacultyRepository;
import ang.mois.pc.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for faculty operations
 */
@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final RoomRepository roomRepository;
    private final FacultyMapper facultyMapper;
    private final ServiceHelper serviceHelper;

    public FacultyService(FacultyRepository facultyRepository, RoomRepository roomRepository, FacultyMapper facultyMapper, ServiceHelper serviceHelper) {
        this.facultyRepository = facultyRepository;
        this.roomRepository = roomRepository;
        this.facultyMapper = facultyMapper;
        this.serviceHelper = serviceHelper;
    }

    /**
     * Get faculty by id
     * @param id faculty id
     * @return {@link FacultyResponseDto}
     */
    public FacultyResponseDto getById(Long id) {
       Faculty faculty = serviceHelper.getFaculty(id);
       return facultyMapper.toResponseDto(faculty);
    }

    /**
     * Get all faculties without pagination
     * @return list of {@link FacultyResponseDto}
     */
    public List<FacultyResponseDto> getAll() {
        return facultyMapper.toResponseDtoList(facultyRepository.findAll());
    }

    /**
     * Get all faculties with pagination
     * @param pageable {@link Pageable} with page params
     * @return a {@link Page} of faculty response dtos
     */
    public Page<FacultyResponseDto> getAll(Pageable pageable) {
        Page<Faculty> facultyPage = facultyRepository.findAll(pageable);
        return facultyPage.map(facultyMapper::toResponseDto);
    }

    /**
     * Create and save a new faculty
     * @param facultyRequestDto request dto with faculty parameters
     * @return saved {@link FacultyResponseDto}
     */
    public FacultyResponseDto save(FacultyRequestDto facultyRequestDto) {
        Faculty faculty = facultyMapper.toEntity(facultyRequestDto);
        return facultyMapper.toResponseDto(facultyRepository.save(faculty));
    }

    /**
     * Delete faculty by id with validation checks
     * @param id faculty id
     */
    public void delete(Long id) {
        // check if faculty even exists
        if (!facultyRepository.existsById(id)) {
            throw new EntityNotFoundException("Faculty not found: " + id);
        }

        // delete only if no room references this faculty as an FK
        if (roomRepository.existsByFacultyId(id)) {
            throw new FKConflictException(
                    "Cannot delete faculty with id:  " + id +", because there are still rooms associated with it."
            );
        }
        // actual delete
        facultyRepository.deleteById(id);
    }

    /**
     * Update existing faculty
     * @param id faculty id
     * @param facultyRequestDto request dto with faculty parameters to update
     * @return updated {@link FacultyResponseDto}
     */
    public FacultyResponseDto update(Long id, FacultyRequestDto facultyRequestDto) {
        Faculty faculty = serviceHelper.getFaculty(id);

        // merge entities - basically copy non-null values to existing faculty
        facultyMapper.updateEntityFromDto(facultyRequestDto, faculty);

        return facultyMapper.toResponseDto(facultyRepository.save(faculty));
    }
}