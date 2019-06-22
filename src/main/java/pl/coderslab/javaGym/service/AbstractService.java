package pl.coderslab.javaGym.service;

import java.util.List;

public interface AbstractService<T> {

    List<T> findAll();

    T findById(Long Id);

    T save(T t);

    void deleteById(Long id);

}
