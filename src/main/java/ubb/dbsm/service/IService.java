package ubb.dbsm.service;

import ubb.dbsm.exceptions.DatabaseError;

import java.util.List;
import java.util.Optional;

public interface IService<ID, T> {
    Optional<T> save(T entity) throws DatabaseError;
    Optional<T> update(T entity) throws DatabaseError ;
    void delete(T entity) throws DatabaseError ;
    Optional<T> find(ID id) throws DatabaseError ;
    List<T> findAll() throws DatabaseError ;
}
