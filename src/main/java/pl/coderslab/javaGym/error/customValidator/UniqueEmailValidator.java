package pl.coderslab.javaGym.error.customValidator;

import org.springframework.beans.factory.annotation.Autowired;
import pl.coderslab.javaGym.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        List<String> allUsersEmails = userService.getAllUsersEmails();
        if (allUsersEmails.contains(email)) {
            return false;
        }
        return true;
    }
}
