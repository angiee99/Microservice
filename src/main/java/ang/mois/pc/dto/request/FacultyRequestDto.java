package ang.mois.pc.dto.request;

import ang.mois.pc.validation.ValidationGroups;
import jakarta.validation.constraints.*;

import java.sql.Time;

/**
 * DTO for faculty create/update requests
 * <p> Validation rules are also defined here.
 * Some are specific only for create operations are annotated with {@link ValidationGroups.OnCreate}.
 * Others are default and are used in every case.
 * </p>
 *  All fields are mandatory on creation.
 * @param name faculty name
 * @param shortcut abbreviation
 * @param email faculty email
 * @param reservationTimeStart the time when the first reservation is possible
 * @param reservationTimeEnd the time util when a reservation is possible
 * @param maxUserReservationCount max reservation count overall
 * @param maxUserReservationTime max time per one reservation in minutes
 * @param maxUserReservationTimeWeekly max time for all reservations per week per user in minutes
 */
public record FacultyRequestDto(

        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Name is mandatory")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,


        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Shortcut is mandatory")
        @Size(min = 1, max = 10, message = "Shortcut must be between 1 and 10 characters")
        String shortcut,

        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Email is mandatory")
        // checks for a pattern that an email must follow
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Email must be a valid address")
        String email,

        @NotNull(groups = ValidationGroups.OnCreate.class, message = "Reservation start time is mandatory")
        Time reservationTimeStart,

        @NotNull(groups = ValidationGroups.OnCreate.class, message = "Reservation end time is mandatory")
        Time reservationTimeEnd,

        @Min(value = 0, message = "Reservation count must be zero or positive")
        @NotNull(groups = ValidationGroups.OnCreate.class, message = "Max user reservation count is mandatory")
        Integer maxUserReservationCount,

        @Min(value = 0, message = "Reservation time must be zero or positive")
        @NotNull(groups = ValidationGroups.OnCreate.class, message = "Max user reservation time is mandatory")
        Integer maxUserReservationTime,

        @Min(value = 0, message = "Weekly reservation time must be zero or positive")
        @NotNull(groups = ValidationGroups.OnCreate.class, message = "Max user reservation time weekly is mandatory")
        Integer maxUserReservationTimeWeekly
) {
}
