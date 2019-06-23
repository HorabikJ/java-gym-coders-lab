package pl.coderslab.javaGym.service;

public interface AbstractStandardService<T> extends AbstractService<T>  {

    T save(T t);

}
