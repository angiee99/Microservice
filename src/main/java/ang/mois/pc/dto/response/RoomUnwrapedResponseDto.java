package ang.mois.pc.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO with all Room fields (unwrapped faculty)
 * @param id
 * @param faculty
 * @param name
 * @param createdAt
 */
public record RoomUnwrapedResponseDto(
        Long id,
        FacultyShortResponseDto faculty,
        String name,
        LocalDateTime createdAt
) {}