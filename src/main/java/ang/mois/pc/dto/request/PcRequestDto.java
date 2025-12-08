package ang.mois.pc.dto.request;


import ang.mois.pc.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for PC create/update requests.
 * <p> It includes validation rules.
 *  Some are specific only for create operations are annotated with {@link ValidationGroups.OnCreate}.
 *  Others are default and are used in every case.
 *  </p>
 * All fields are mandatory on creation.
 * @param name the name of the PC, must be between 2 and 100 characters
 * @param available indicates whether the PC is available
 * @param computerRoomId the ID of the associated computer room
 * @param configId the ID of the PC configuration
 */
public record PcRequestDto(

        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Name is mandatory")
        String name,

        @NotNull(groups = ValidationGroups.OnCreate.class, message = "Available is mandatory")
        Boolean available,

        @NotNull(groups = ValidationGroups.OnCreate.class, message = "Computer Room Id is mandatory")
        Long computerRoomId,

        @NotNull(groups = ValidationGroups.OnCreate.class, message = "Computer Config Id is mandatory")
        Long configId
) {
}
