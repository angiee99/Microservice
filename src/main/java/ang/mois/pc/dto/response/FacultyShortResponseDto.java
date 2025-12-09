package ang.mois.pc.dto.response;

/** Response DTO with main faculty fields only
 *
 * @param facultyId
 * @param name
 * @param shortcut
 */
public record FacultyShortResponseDto(
        Long facultyId,
        String name,
        String shortcut
) {}
