package ang.mois.pc.mapper;

import ang.mois.pc.dto.request.FacultyRequestDto;
import ang.mois.pc.dto.response.FacultyResponseDto;
import ang.mois.pc.entity.Faculty;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyMapper {

    // Requests: dto to entity (ignore id and automatically set creation timestamp)
    @Mapping(target = "id", ignore = true) // don’t copy from DTO
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())") // set automatically
    Faculty toEntity(FacultyRequestDto dto);

    // Update: copy non-null values from dto to existing entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(FacultyRequestDto dto, @MappingTarget Faculty entity);

    // Responses: entity to response dto
    FacultyResponseDto toResponseDto(Faculty entity);
    List<FacultyResponseDto> toResponseDtoList(List<Faculty> entities);
}
