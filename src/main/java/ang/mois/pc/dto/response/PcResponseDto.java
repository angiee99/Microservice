package ang.mois.pc.dto.response;
import java.time.LocalDateTime;

/**
 * Response DTO with all PC fields (wrapped)
 * @param id
 * @param name
 * @param available
 * @param computerRoomId
 * @param configId
 * @param createdAt
 */
public record PcResponseDto(
        Long id,
        String name,
        boolean available,
        Long computerRoomId,
        Long configId,
        LocalDateTime createdAt
) {}
