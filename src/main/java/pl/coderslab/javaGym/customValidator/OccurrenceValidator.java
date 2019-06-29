package pl.coderslab.javaGym.customValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;

public class OccurrenceValidator implements ConstraintValidator<Occurrence, Integer> {

    private ArrayList<Integer> occurrence = new ArrayList<>(Arrays.asList(1,7));

    @Override
    public void initialize(Occurrence constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return occurrence.contains(value);
    }

}
