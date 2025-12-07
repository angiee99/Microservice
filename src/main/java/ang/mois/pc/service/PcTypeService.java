package ang.mois.pc.service;

import ang.mois.pc.dto.request.PcTypeRequestDto;
import ang.mois.pc.dto.response.PcTypeResponseDto;
import ang.mois.pc.entity.PcType;
import ang.mois.pc.mapper.PcTypeMapper;
import ang.mois.pc.repository.PcRepository;
import ang.mois.pc.repository.PcTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PcTypeService {
    private final PcTypeRepository pcTypeRepository;
    private final PcRepository pcRepository;
    private final PcTypeMapper pcTypeMapper;

    public PcTypeService(PcTypeRepository pcTypeRepository, PcRepository pcRepository, PcTypeMapper pcTypeMapper) {
        this.pcTypeRepository = pcTypeRepository;
        this.pcRepository = pcRepository;
        this.pcTypeMapper = pcTypeMapper;
    }

    public List<PcTypeResponseDto> getAll() {
        return pcTypeMapper.toResponseDtoList(pcTypeRepository.findAll());
    }

    public Page<PcTypeResponseDto> getAll(Pageable pageable) {
        Page<PcType> pcTypePage = pcTypeRepository.findAll(pageable);
        return pcTypePage.map(pcTypeMapper::toResponseDto);
    }

    public PcTypeResponseDto getById(Long typeId) {
        PcType type = getPcType(typeId);
        return pcTypeMapper.toResponseDto(type);
    }

    public PcTypeResponseDto save(PcTypeRequestDto type) {
        PcType pcType = pcTypeMapper.toEntity(type);
        return pcTypeMapper.toResponseDto(pcTypeRepository.save(pcType));
    }

    public void delete(Long typeId) {
        // check if type exists
        if(!pcTypeRepository.existsById(typeId)) {
            throw new IllegalArgumentException("Configuration with id " + typeId + " does not exist");
        }

        // delete if no computer references this type?
        if(pcRepository.existsByPcTypeId(typeId)) {
            throw new FKConflictException(
                    "Cannot delete configuration with id: " + typeId +" , because there are still computers associated with it."
            );
        }
        pcTypeRepository.deleteById(typeId);
    }

    public PcTypeResponseDto update(Long id, PcTypeRequestDto pcTypeDto) {
        PcType pcTypeEntity = getPcType(id);

        // map by copying non-null values from update dto
        pcTypeMapper.updateEntityFromDto(pcTypeDto, pcTypeEntity);

        return pcTypeMapper.toResponseDto(pcTypeRepository.save(pcTypeEntity));
    }

    private PcType getPcType(Long id) {
        return pcTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration with id " + id + " does not exist"));
    }
}
