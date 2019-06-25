package pl.coderslab.javaGym.service.userService;

import pl.coderslab.javaGym.service.AbstractService;

public interface AbstractUserService<T> extends AbstractService<T> {

    T save(T t, Boolean asAdmin);

}
