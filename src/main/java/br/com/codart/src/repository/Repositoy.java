package br.com.codart.src.repository;

import java.util.Optional;

public interface Repositoy<T> {

    void save(T entity);

    Optional<T> findById(Long id);

    void findAll();

    void deleteById(Long id);


}
