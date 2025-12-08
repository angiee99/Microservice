package ang.mois.pc.dto.request;

import ang.mois.pc.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for computer room create/update requests.
 * <p>
 * It includes validation rules.
 * Some are specific only for create operations and are annotated with {@link ValidationGroups.OnCreate}.
 * Others are default and are used in every case.
 * </p>
 * All fields are mandatory on creation.
 * @param name room name
 * @param facultyId faculty id of where the room is located
 */
public record RoomRequestDto(

        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Name is mandatory")
        String name,

        @NotNull(groups = ValidationGroups.OnCreate.class, message = "Faculty Id is mandatory")
        Long facultyId) {
}
