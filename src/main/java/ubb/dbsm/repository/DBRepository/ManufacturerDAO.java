package ubb.dbsm.repository.DBRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.repository.model.IManufacturerRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class ManufacturerDAO implements IManufacturerRepository {
    @PersistenceContext
    protected EntityManager em;

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
    public Optional<Manufacturer> find(Integer id) {
        log.debug("Finding Manufacturer by ID {}", id);
        return Optional.ofNullable(em.find(Manufacturer.class, id));
    }

    @Override
    public List<Manufacturer> findAll() {
        log.debug("Finding all Manufacturers");
        List<Manufacturer> manufacturerList = em.createQuery("SELECT m FROM Manufacturer m", Manufacturer.class).getResultList();
        log.debug("Found {} Manufacturers", manufacturerList.size());
        return manufacturerList;
    }

    @Override
    public Optional<Manufacturer> findByName(String name) {
        log.debug("Finding Manufacturer by name {}", name);
        List<Manufacturer> manufacturerList = em.createQuery("SELECT m FROM Manufacturer m WHERE name = :name", Manufacturer.class)
                .setParameter("name", name).getResultList();
        log.debug("Found {} Manufacturers with the name {}", manufacturerList.size(), name);
        return manufacturerList.stream().findFirst();
    }
}
