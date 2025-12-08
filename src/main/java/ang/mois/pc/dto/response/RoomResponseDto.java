package ang.mois.pc.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO with all Room fields (wrapped)
 * @param id
 * @param facultyId
 * @param name
 * @param createdAt
 */
public record RoomResponseDto(
        Long id,
        Long facultyId,
        String name,
        LocalDateTime createdAt
) {}

