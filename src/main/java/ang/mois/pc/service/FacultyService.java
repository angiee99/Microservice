package ang.mois.pc.service;

import ang.mois.pc.dto.request.FacultyRequestDto;
import ang.mois.pc.dto.response.FacultyResponseDto;
import ang.mois.pc.entity.Faculty;
import ang.mois.pc.mapper.FacultyMapper;
import ang.mois.pc.repository.FacultyRepository;
import ang.mois.pc.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final RoomRepository roomRepository;
    private final FacultyMapper facultyMapper;

    public FacultyService(FacultyRepository facultyRepository, RoomRepository roomRepository, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.roomRepository = roomRepository;
        this.facultyMapper = facultyMapper;
    }

    public FacultyResponseDto getById(Long id) {
       Faculty faculty = getFaculty(id);
       return facultyMapper.toResponseDto(faculty);
    }

    public List<FacultyResponseDto> getAll() {
        return facultyMapper.toResponseDtoList(facultyRepository.findAll());
    }

    public Page<FacultyResponseDto> getAll(Pageable pageable) {
        Page<Faculty> facultyPage = facultyRepository.findAll(pageable);
        return facultyPage.map(facultyMapper::toResponseDto);
    }

    public FacultyResponseDto save(FacultyRequestDto facultyRequestDto) {
        Faculty faculty = facultyMapper.toEntity(facultyRequestDto);
        return facultyMapper.toResponseDto(facultyRepository.save(faculty));
    }

    public void delete(Long id) {
        // check if faculty even exists
        if (!facultyRepository.existsById(id)) {
            throw new IllegalArgumentException("Faculty not found: " + id);
        }

        // delete only if no room references this faculty as an FK
        if (roomRepository.existsByFacultyId(id)) {
            throw new FKConflictException(
                    "Cannot delete faculty with id:  " + id +", because there are still rooms associated with it."
            );
        }

        facultyRepository.deleteById(id);
    }

    public FacultyResponseDto update(Long id, FacultyRequestDto facultyRequestDto) {
        Faculty faculty = getFaculty(id);

        // merge entities - basically copy non-null values to existing faculty
        facultyMapper.updateEntityFromDto(facultyRequestDto, faculty);

        return facultyMapper.toResponseDto(facultyRepository.save(faculty));
    }

    private Faculty getFaculty(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Faculty with id " + id + " does not exist"));
    }
}