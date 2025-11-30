package ang.mois.pc.dto.response;

import java.time.LocalDateTime;

public record RoomUnwrapedResponseDto(
        Long id,
        FacultyShortResponseDto faculty,
        String name,
        LocalDateTime createdAt
) {}