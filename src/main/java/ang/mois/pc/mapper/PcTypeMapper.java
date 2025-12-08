package ang.mois.pc.mapper;

import ang.mois.pc.dto.request.PcTypeRequestDto;
import ang.mois.pc.dto.response.PcTypeResponseDto;
import ang.mois.pc.entity.PcType;
import org.mapstruct.*;

import java.util.List;
/**
 * Mapper for transforming PC Configuration entities to dtos and vise versa
 */
@Mapper(componentModel = "spring")
public interface PcTypeMapper {
    // Create Request: dto to entity
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())") // set automatically
    PcType toEntity(PcTypeRequestDto dto);

    // Update Request: merge non-null fields into existing entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PcTypeRequestDto dto, @MappingTarget PcType entity);

    // Responses: entity to dto
    PcTypeResponseDto toResponseDto(PcType entity);
    List<PcTypeResponseDto> toResponseDtoList(List<PcType> entities);
}
