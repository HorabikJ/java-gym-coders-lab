package pl.coderslab.javaGym.service;

import java.util.List;

public interface AbstractService<T> {

    List<T> findAll();

    T findById(Long Id);

    void deleteById(Long id);

}
