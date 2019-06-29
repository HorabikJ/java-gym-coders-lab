package pl.coderslab.javaGym.customValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OccurrenceValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Occurrence {

    String message() default "*Please provide valid occurrence: 1 for daily or 7 for weekly.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
