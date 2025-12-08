package ang.mois.pc.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO with all PC Type (Configuration) fields
 * @param id
 * @param name
 * @param cpu
 * @param ram
 * @param gpu
 * @param createdAt
 */
public record PcTypeResponseDto(
        Long id,
        String name,
        String cpu,
        String ram,
        String gpu,
        LocalDateTime createdAt
) {}
