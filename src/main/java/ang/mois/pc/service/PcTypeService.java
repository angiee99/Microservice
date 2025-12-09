package ang.mois.pc.service;

import ang.mois.pc.dto.request.PcTypeRequestDto;
import ang.mois.pc.dto.response.PcTypeResponseDto;
import ang.mois.pc.entity.PcType;
import ang.mois.pc.mapper.PcTypeMapper;
import ang.mois.pc.repository.PcRepository;
import ang.mois.pc.repository.PcTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service for PC Type (Configuration) operations
 */
@Service
public class PcTypeService {
    private final PcTypeRepository pcTypeRepository;
    private final PcRepository pcRepository;
    private final PcTypeMapper pcTypeMapper;
    private final ServiceHelper serviceHelper;

    public PcTypeService(PcTypeRepository pcTypeRepository, PcRepository pcRepository, PcTypeMapper pcTypeMapper, ServiceHelper serviceHelper) {
        this.pcTypeRepository = pcTypeRepository;
        this.pcRepository = pcRepository;
        this.pcTypeMapper = pcTypeMapper;
        this.serviceHelper = serviceHelper;
    }

    /**
     * Get all PC types without pagination.
     * @return list of {@link PcTypeResponseDto}
     */
    public List<PcTypeResponseDto> getAll() {
        return pcTypeMapper.toResponseDtoList(pcTypeRepository.findAll());
    }

    /**
     * Get all PC types with pagination support.
     * @param pageable {@link Pageable} with page parameters
     * @return a {@link Page} of {@link PcTypeResponseDto}
     */
    public Page<PcTypeResponseDto> getAll(Pageable pageable) {
        Page<PcType> pcTypePage = pcTypeRepository.findAll(pageable);
        return pcTypePage.map(pcTypeMapper::toResponseDto);
    }

    /**
     * Get a PC type by ID.
     * @param typeId PC type ID
     * @return {@link PcTypeResponseDto}
     */
    public PcTypeResponseDto getById(Long typeId) {
        PcType type = serviceHelper.getType(typeId);
        return pcTypeMapper.toResponseDto(type);
    }

    /**
     * Create a new PC type and store it.
     * @param type request DTO with PC type parameters
     * @return saved {@link PcTypeResponseDto}O
     */
    public PcTypeResponseDto save(PcTypeRequestDto type) {
        PcType pcType = pcTypeMapper.toEntity(type);
        return pcTypeMapper.toResponseDto(pcTypeRepository.save(pcType));
    }

    /**
     * Delete a PC type by ID.
     * Checks that the type exists and that no PCs reference it.
     * @param typeId PC type ID
     * @throws FKConflictException if there are PCs still associated with this type
     */
    public void delete(Long typeId) {
        // check if type exists
        if(!pcTypeRepository.existsById(typeId)) {
            throw new EntityNotFoundException("Configuration with id " + typeId + " does not exist");
        }

        // delete if no computer references this type
        if(pcRepository.existsByPcTypeId(typeId)) {
            throw new FKConflictException(
                    "Cannot delete configuration with id: " + typeId +" , because there are still computers associated with it."
            );
        }
        pcTypeRepository.deleteById(typeId);
    }

    /**
     * Update an existing PC type.
     * @param id PC type ID
     * @param pcTypeDto request DTO with updated PC type parameters
     * @return updated {@link PcTypeResponseDto}
     */
    public PcTypeResponseDto update(Long id, PcTypeRequestDto pcTypeDto) {
        PcType pcTypeEntity = serviceHelper.getType(id);

        // map by copying non-null values from update dto
        pcTypeMapper.updateEntityFromDto(pcTypeDto, pcTypeEntity);

        return pcTypeMapper.toResponseDto(pcTypeRepository.save(pcTypeEntity));
    }

}
