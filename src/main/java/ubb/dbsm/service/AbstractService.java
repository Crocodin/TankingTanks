package ubb.dbsm.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.dbsm.domain.HasID;
import ubb.dbsm.domain.validator.ValidatorStrategy;
import ubb.dbsm.exceptions.DatabaseError;
import ubb.dbsm.exceptions.EntityFoundException;
import ubb.dbsm.repository.IRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractService<ID, T extends HasID<ID>, R extends IRepository<ID, T>> implements IService<ID, T> {
    protected R repository;
    protected ValidatorStrategy<T> validatorStrategy;
    protected static final Logger logger = LogManager.getLogger(AbstractService.class);

    @Override
    public Optional<T> save(T object) {
        logger.debug("Saving object {}", object);
        if (!validatorStrategy.validate(object)) {
            logger.warn("Validation failed for object {}", object);
            return Optional.empty();
        }
        logger.debug("Saving object {}", object);
        return repository.save(object);
    }

    @Override
    public Optional<T> update(T object) {
        logger.debug("Updating object {}", object);
        if (!validatorStrategy.validate(object)) {
            logger.warn("Validation failed for object {}", object);
            return Optional.empty();
        }
        logger.debug("Updating object {}", object);
        return repository.update(object);
    }

    @Override
    public void delete(T entity) throws EntityFoundException {
        logger.debug("Deleting object {}", entity);
        this.find(entity.getId()).ifPresentOrElse(
                 val -> {
                     logger.debug("Deleting object {}", entity);
                     repository.delete(val);
                 },
                ()-> { throw new EntityFoundException("Entity with id " + entity.getId() + " not found"); }
        );
    }

    @Override
    public Optional<T> find(ID id) {
        logger.debug("Finding object {}", id);
        return repository.find(id);
    }

    @Override
    public List<T> findAll() {
        logger.debug("Finding all objects");
        return repository.findAll();
    }
}
