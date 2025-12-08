package ang.mois.pc.dto.response;

import java.sql.Time;
import java.time.LocalDateTime;

/**Response DTO with all faculty fields
 *
 * @param id
 * @param name
 * @param shortcut
 * @param email
 * @param reservationTimeStart
 * @param reservationTimeEnd
 * @param maxUserReservationCount
 * @param maxUserReservationTime
 * @param maxUserReservationTimeWeekly
 * @param createdAt
 */
public record FacultyResponseDto(
         Long id,
         String name,
         String shortcut,
         String email,
         Time reservationTimeStart,
         Time reservationTimeEnd,
         Integer maxUserReservationCount,
         Integer maxUserReservationTime,
         Integer maxUserReservationTimeWeekly,
         LocalDateTime createdAt
) {
}
