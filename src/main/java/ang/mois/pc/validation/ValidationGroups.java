package ang.mois.pc.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

// Interface acts as markers for validation "cases"
public interface ValidationGroups {

    // validation rules to apply only on creation
    interface OnCreate {}

    // this tells the validator to run OnCreate first, then default
    // if OnCreate fails, it stops and never checks default
    @GroupSequence({OnCreate.class, Default.class})
    interface OnCreateSequence {}
}
