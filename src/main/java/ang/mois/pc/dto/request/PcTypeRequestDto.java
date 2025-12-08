package ang.mois.pc.dto.request;

import ang.mois.pc.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for PC type (configuration) create/update requests.
 * <p>
 * It includes validation rules.
 * Some are specific only for create operations and are annotated with {@link ValidationGroups.OnCreate}.
 * Others are default and are used in every case.
 * </p>
 * All fields are mandatory on creation.
 * @param name the name of the PC type; must be between 2 and 100 characters
 * @param cpu the CPU specification
 * @param ram the RAM specification
 * @param gpu the GPU specification
 */
public record PcTypeRequestDto(

        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Name is mandatory")
        String name,

        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Cpu is mandatory")
        String cpu,

        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Ram is mandatory")
        String ram,

        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "Gpu is mandatory")
        String gpu
) {
}

