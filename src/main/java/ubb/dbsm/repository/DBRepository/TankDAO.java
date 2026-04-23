package ubb.dbsm.repository.DBRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.exceptions.DatabaseError;
import ubb.dbsm.repository.model.ITankRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class TankDAO implements ITankRepository {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public List<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer) {
    log.debug("Entering findByNameAndManufacturer with name={} and manufacturer={}", name, manufacturer);
        List<Tank> tankList = em.createQuery("SELECT t FROM Tank t JOIN t.manufacturer m WHERE LOWER(t.name) LIKE LOWER(:name) AND m = :manufacturer", Tank.class)
                .setParameter("name", "%" + name + "%")
                .setParameter("manufacturer", manufacturer)
                .getResultList();
        log.debug("Found {} tanks with name like '{}' for manufacturer {}", tankList.size(), name, manufacturer);
        return tankList;
    }

    @Override
    public Optional<Tank> save(Tank entity) throws DatabaseError {
        log.debug("Entering save with entity {}", entity);
        em.persist(entity);
        log.debug("Saved entity {} with name {}", entity, entity.getName());
        return Optional.of(entity);
    }

    @Override
    public Optional<Tank> update(Tank entity) {
        log.debug("Entering update with entity {}", entity);
        em.merge(entity);
        log.debug("Updated entity {} with name {}", entity, entity.getName());
        return Optional.of(entity);
    }

    @Override
    public void delete(Tank entity) {
        log.debug("Entering delete with entity {}", entity);
        em.remove(em.contains(entity) ? entity : em.merge(entity));
        log.info("Deleted entity {} with name {}", entity, entity.getName());
    }

    @Override
    public Optional<Tank> find(Integer id) {
        log.debug("Entering find with entity {}", id);
        return Optional.ofNullable(em.find(Tank.class, id));
    }

    @Override
    public List<Tank> findAll() {
        log.debug("Entering findAll");
        List<Tank> tankList = em.createQuery("SELECT t FROM Tank t", Tank.class).getResultList();
        log.debug("Fetched {} tanks", tankList.size());
        return tankList;
    }
}
