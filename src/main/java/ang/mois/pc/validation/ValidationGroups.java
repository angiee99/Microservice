package ang.mois.pc.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

// Interface acts as markers for validation "cases"
public interface ValidationGroups {

    // Validation rules to apply only on creation
    interface OnCreate {}

    // The GroupSequence defines the order of validations, so tells the validator to run OnCreate first, then default.
    // If OnCreate validation fails, it stops and doesn't check default
    @GroupSequence({OnCreate.class, Default.class})
    interface OnCreateSequence {}
}
