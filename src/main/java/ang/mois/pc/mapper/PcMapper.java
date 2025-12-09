package ang.mois.pc.mapper;
import ang.mois.pc.dto.request.PcRequestDto;
import ang.mois.pc.dto.response.PcResponseDto;
import ang.mois.pc.dto.response.PcUnwrappedResponseDto;
import ang.mois.pc.entity.Pc;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper for transforming PC entities to dtos and vise versa
 */
@Mapper(componentModel = "spring")
public interface PcMapper {

    // Create Request: dto to entity - copy all fields
    @Mapping(target = "id", ignore = true) // don’t copy from DTO
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())") // set automatically
    Pc toEntity(PcRequestDto dto);

    // Update Request: Merge non-null fields into existing entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PcRequestDto dto, @MappingTarget Pc entity);

    // Response: Entity -> Response DTO with flattened FK
    @Mapping(target = "computerRoomId", source = "room.id")
    @Mapping(target = "configId", source = "pcType.id")
    PcResponseDto toResponseDto(Pc entity);

    List<PcResponseDto> toResponseDtoList(List<Pc> entities);

    // Response: Entity -> Response Unwrapped Dto
    @Mapping(target = "computerRoom", source = "room")
    @Mapping(target = "computerRoom.faculty.facultyId", source = "room.faculty.id")
    @Mapping(target = "computerRoom.faculty.name", source = "room.faculty.name")
    @Mapping(target = "computerRoom.faculty.shortcut", source = "room.faculty.shortcut")
    @Mapping(target = "computerConfig", source = "pcType")
    PcUnwrappedResponseDto toUnwrappedResponseDto(Pc entity);
}