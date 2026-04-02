package ubb.dbsm.service;

import ubb.dbsm.exceptions.DatabaseError;
import ubb.dbsm.exceptions.EntityFoundException;

import java.util.List;
import java.util.Optional;

public interface IService<ID, T> {
    Optional<T> save(T entity);
    Optional<T> update(T entity);
    void delete(T entity) throws EntityFoundException;
    Optional<T> find(ID id);
    List<T> findAll();
}
