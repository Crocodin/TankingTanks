package ubb.dbsm.repository.DBRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.exceptions.DatabaseError;
import ubb.dbsm.repository.model.ITankRepository;
import ubb.dbsm.utils.JPAUtils;

import java.util.List;
import java.util.Optional;

public class TankDAO implements ITankRepository {
    private static final Logger logger = LogManager.getLogger(TankDAO.class);
    protected final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();

    @Override
    public List<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer) {
        logger.debug("Entering findByNameAndManufacturer with name={} and manufacturer={}", name, manufacturer);
        try (EntityManager em = emf.createEntityManager()) {
            List<Tank> tankList = em.createQuery("SELECT t FROM Tank t JOIN t.manufacturer m WHERE LOWER(t.name) LIKE LOWER(:name) AND m = :manufacturer", Tank.class)
                    .setParameter("name", "%" + name + "%")
                    .setParameter("manufacturer", manufacturer)
                    .getResultList();
            logger.info("Found {} tanks with name like '{}' for manufacturer {}", tankList.size(), name, manufacturer);
            return tankList;
        } catch (Exception e) {
            logger.error("Failed in findByNameAndManufacturer with name={} and manufacturer={}", name, manufacturer, e);
            throw e;
        }
    }

    @Override
    public Optional<Tank> save(Tank entity) throws DatabaseError {
        logger.debug("Entering save with entity {}", entity);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            logger.info("Saved entity {} with name {}", entity, entity.getName());
            return Optional.of(entity);
        } catch (Exception e) {
            logger.error("Failed in save with entity {} with name {}", entity, entity.getName(), e);
            throw e;
        }
    }

    @Override
    public Optional<Tank> update(Tank entity) {
        logger.debug("Entering update with entity {}", entity);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
            logger.info("Updated entity {} with name {}", entity, entity.getName());
            return Optional.of(entity);
        } catch (Exception e) {
            logger.error("Failed in update with entity {} with name {}", entity, entity.getName(), e);
            throw e;
        }
    }

    @Override
    public void delete(Tank entity) {
        logger.debug("Entering delete with entity {}", entity);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Tank managedTank = em.merge(entity);  // re-attach the detached entity
            em.remove(managedTank);               // remove the managed version
            em.getTransaction().commit();
            logger.info("Deleted entity {} with name {}", entity, entity.getName());
        } catch (Exception e) {
            logger.error("Failed in delete with entity {} with name {}", entity, entity.getName(), e);
            throw e;
        }
    }

    @Override
    public Optional<Tank> find(Integer integer) {
        logger.debug("Entering find with entity {}", integer);
        try (EntityManager em = emf.createEntityManager()) {
            Tank tank = em.find(Tank.class, integer);
            logger.debug("Found entity {} with name {}", tank, tank.getName());
            return Optional.of(tank);
        } catch (Exception e) {
            logger.error("Failed in find with entity {}", integer, e);
            throw e;
        }
    }

    @Override
    public List<Tank> findAll() {
        logger.debug("Entering findAll");
        try (EntityManager em = emf.createEntityManager()) {
            List<Tank> tankList = em.createQuery("SELECT t FROM Tank t").getResultList();
            logger.debug("Fetched {} tanks", tankList.size());
            return tankList;
        } catch (Exception e) {
            logger.error("Failed in findAll", e);
            throw e;
        }
    }
}
