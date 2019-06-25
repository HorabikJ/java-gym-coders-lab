package pl.coderslab.javaGym.service.emailService;

import pl.coderslab.javaGym.entity.email.ConfirmationEmail;
import pl.coderslab.javaGym.service.AbstractService;

public interface AbstractConfirmationEmailService<T> extends AbstractService<ConfirmationEmail> {

    T save (T t);

}
