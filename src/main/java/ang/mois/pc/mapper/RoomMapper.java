package ang.mois.pc.mapper;

import ang.mois.pc.dto.request.RoomRequestDto;
import ang.mois.pc.dto.response.RoomResponseDto;
import ang.mois.pc.entity.Room;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    // Create Request: dto to entity
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())") // set automatically
    Room toEntity(RoomRequestDto dto);

    // Update: merge non-null fields of dto into existing entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RoomRequestDto dto, @MappingTarget Room entity);

    // Responses: entity to response dto
    @Mapping(target = "facultyId", source = "faculty.id")
    RoomResponseDto toResponseDto(Room entity);

    List<RoomResponseDto> toResponseDtoList(List<Room> entities);
}