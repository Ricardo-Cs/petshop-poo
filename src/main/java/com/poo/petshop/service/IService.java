package com.poo.petshop.service;

import java.util.List;
import java.util.Optional;

public interface IService<T> {

    List<T> findAll();
    Optional<T> findById(Long id);
    List<T> findByName(String name);
    T save(T entity);
    T update(T entity);
    void delete(Long id);

}
