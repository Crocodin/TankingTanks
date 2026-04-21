package ubb.dbsm.repository.DBRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.repository.IRepository;
import ubb.dbsm.repository.model.IManufacturerRepository;
import ubb.dbsm.utils.JPAUtils;

import java.util.List;
import java.util.Optional;

public class ManufacturerDAO implements IManufacturerRepository {
    private static final Logger logger = LogManager.getLogger(ManufacturerDAO.class);
    protected final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();

    @Override
    public Optional<Manufacturer> save(Manufacturer entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Manufacturer> update(Manufacturer entity) {
        return Optional.empty();
    }

    @Override
    public void delete(Manufacturer entity) { }

    @Override
    public Optional<Manufacturer> find(Integer integer) {
        logger.debug("Finding Manufacturer by ID {}", integer);
        try (EntityManager em = emf.createEntityManager()) {
            Manufacturer manufacturer = em.find(Manufacturer.class, integer);
            if (manufacturer == null) {
                logger.warn("Manufacturer with ID {} not found", integer);
                return Optional.empty();
            }
            logger.info("Manufacturer with ID {} found", integer);
            return Optional.of(manufacturer);
        } catch (Exception e) {
            logger.error("Failed to find Manufacturer by ID {}", integer, e);
            throw e;
        }
    }

    @Override
    public List<Manufacturer> findAll() {
        logger.debug("Finding all Manufacturers");
        try (EntityManager em = emf.createEntityManager()) {
            List<Manufacturer> manufacturerList = em.createQuery("SELECT m FROM Manufacturer m", Manufacturer.class).getResultList();
            logger.info("Found {} Manufacturers", manufacturerList.size());
            return manufacturerList;
        } catch (Exception e) {
            logger.error("Failed to find all Manufacturers", e);
            throw e;
        }
    }

    @Override
    public Optional<Manufacturer> findByName(String name) {
        logger.debug("Finding Manufacturer by name {}", name);
        try (EntityManager em = emf.createEntityManager()) {
            List<Manufacturer> manufacturerList = em.createQuery("SELECT m FROM Manufacturer m WHERE name = :name", Manufacturer.class)
                    .setParameter("name", name).getResultList();
            logger.info("Found {} Manufacturers with the name {}", manufacturerList.size(), name);
            return manufacturerList.stream().findFirst();
        } catch (Exception e) {
            logger.error("Failed to find Manufacturer by name {}", name, e);
            throw e;
        }
    }
}
