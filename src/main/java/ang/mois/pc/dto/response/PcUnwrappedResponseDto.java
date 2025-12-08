package ang.mois.pc.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO with all PC fields (unwrapped computer room and configuration)
 * @param id
 * @param name
 * @param available
 * @param computerRoom
 * @param computerConfig
 * @param createdAt
 */
public record PcUnwrappedResponseDto(
        Long id,
        String name,
        boolean available,
        RoomUnwrapedResponseDto computerRoom,
        PcTypeResponseDto computerConfig,
        LocalDateTime createdAt
) {
}
