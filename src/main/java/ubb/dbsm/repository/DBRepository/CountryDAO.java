package ubb.dbsm.repository.DBRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ubb.dbsm.domain.Country;
import ubb.dbsm.repository.IRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class CountryDAO implements IRepository<Integer, Country> {
    @PersistenceContext
    private EntityManager em;

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
        log.debug("Looking up Country with id={}", id);
        return Optional.ofNullable(em.find(Country.class, id));
    }

    @Override
    public List<Country> findAll() {
        log.debug("Fetching all countries");
        List<Country> countries = em.createQuery("SELECT c FROM Country c", Country.class)
                .getResultList();
        log.debug("Fetched {} countries", countries.size());
        return countries;
    }
}
