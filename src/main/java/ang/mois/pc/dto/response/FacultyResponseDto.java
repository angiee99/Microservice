package ang.mois.pc.dto.response;

import java.sql.Time;
import java.time.LocalDateTime;

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
