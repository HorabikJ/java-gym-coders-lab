package pl.coderslab.javaGym.service.confirmationEmailService;

import pl.coderslab.javaGym.entity.confirmationEmail.ConfirmationEmail;
import pl.coderslab.javaGym.service.AbstractService;

public interface AbstractConfirmationEmailService<T> extends AbstractService<ConfirmationEmail> {

    T save (T t);

}
