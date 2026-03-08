package ubb.dbsm.service;

import ubb.dbsm.domain.HasID;
import ubb.dbsm.domain.validator.ValidatorStrategy;
import ubb.dbsm.exceptions.DatabaseError;
import ubb.dbsm.exceptions.EntityFoundException;
import ubb.dbsm.repository.IRepository;

import java.util.List;
import java.util.Optional;

abstract class AbstractService<ID, T extends HasID<ID>> implements IService<ID, T> {
    IRepository<ID, T> repository;
    ValidatorStrategy<T> validatorStrategy;

    @Override
    public Optional<T> save(T object) throws DatabaseError {
        if (!validatorStrategy.validate(object)) return Optional.empty();
        return repository.save(object);
    }

    @Override
    public Optional<T> update(T object) throws DatabaseError  {
        if (!validatorStrategy.validate(object)) return Optional.empty();
        return repository.update(object);
    }

    @Override
    public void delete(T entity) throws DatabaseError {
        this.find(entity.getId()).ifPresentOrElse(
                 val -> repository.delete(val),
                ()-> { throw new EntityFoundException("Entity with id " + entity.getId() + " not found"); }
        );
    }

    @Override
    public Optional<T> find(ID id) throws DatabaseError {
        return repository.find(id);
    }

    @Override
    public List<T> findAll() throws DatabaseError  {
        return repository.findAll();
    }
}
