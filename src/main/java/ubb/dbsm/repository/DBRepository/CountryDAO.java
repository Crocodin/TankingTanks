package ubb.dbsm.repository.DBRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.dbsm.domain.Country;
import ubb.dbsm.repository.IRepository;
import ubb.dbsm.utils.JPAUtils;

import java.util.List;
import java.util.Optional;

public class CountryDAO implements IRepository<Integer, Country> {
    private final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();
    private static final Logger logger = LogManager.getLogger(CountryDAO.class);

    @Override
    public Optional<Country> save(Country entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Country> update(Country entity) {
        return Optional.empty();
    }

    @Override
    public void delete(Country entity) { }

    @Override
    public Optional<Country> find(Integer id) {
        logger.debug("Looking up Country with id={}", id);
        try (EntityManager em = emf.createEntityManager()) {
            Country country = em.find(Country.class, id);
            if (country == null) {
                logger.warn("No Country found with id={}", id);
                return Optional.empty();
            }
            logger.debug("Found Country: {}", country);
            return Optional.of(country);
        } catch (Exception e) {
            logger.error("Error finding Country with id={}", id, e);
            throw e;
        }
    }

    @Override
    public List<Country> findAll() {
        logger.debug("Fetching all countries");
        try (EntityManager em = emf.createEntityManager()) {
            List<Country> countries = em.createQuery("SELECT c FROM Country c", Country.class)
                    .getResultList();
            logger.info("Fetched {} countries", countries.size());
            return countries;
        } catch (Exception e) {
            logger.error("Error fetching all countries", e);
            throw e;
        }
    }
}
