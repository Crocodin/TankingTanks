package ubb.dbsm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import ubb.dbsm.domain.HasID;
import ubb.dbsm.domain.validator.ValidatorStrategy;
import ubb.dbsm.exceptions.EntityFoundException;
import ubb.dbsm.repository.IRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractService<ID, T extends HasID<ID>, R extends IRepository<ID, T>> implements IService<ID, T> {
    protected R repository;
    protected ValidatorStrategy<T> validatorStrategy;

    @Override
    @CacheEvict(value = {"manufacturerPages", "tankPages"}, allEntries = true)
    public Optional<T> save(T object) {
        log.debug("Saving object {}", object);
        if (!validatorStrategy.validate(object)) {
            log.warn("Validation failed for object {}", object);
            return Optional.empty();
        }
        log.debug("Saving object {}", object);
        return repository.save(object);
    }

    @Override
    @CacheEvict(value = {"manufacturerPages", "tankPages"}, allEntries = true)
    public Optional<T> update(T object) {
        log.debug("Updating object {}", object);
        if (!validatorStrategy.validate(object)) {
            log.warn("Validation failed for object {}", object);
            return Optional.empty();
        }
        log.debug("Updating object {}", object);
        return repository.update(object);
    }

    @Override
    @CacheEvict(value = {"manufacturerPages", "tankPages"}, allEntries = true)
    public void delete(T entity) throws EntityFoundException {
        log.debug("Deleting object {}", entity);
        this.find(entity.getId()).ifPresentOrElse(
                 val -> {
                     log.debug("Deleting object {}", entity);
                     repository.delete(val);
                 },
                ()-> { throw new EntityFoundException("Entity with id " + entity.getId() + " not found"); }
        );
    }

    @Override
    public Optional<T> find(ID id) {
        log.debug("Finding object {}", id);
        return repository.find(id);
    }

    @Override
    public List<T> findAll() {
        log.debug("Finding all objects");
        return repository.findAll();
    }
}
