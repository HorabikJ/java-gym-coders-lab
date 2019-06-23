package pl.coderslab.javaGym.service;

public interface AbstractUserService<T> extends AbstractService<T> {

    T save(T t, Boolean asAdmin);

}
