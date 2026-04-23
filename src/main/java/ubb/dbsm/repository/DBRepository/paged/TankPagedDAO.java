package ubb.dbsm.repository.DBRepository.paged;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.repository.DBRepository.TankDAO;
import ubb.dbsm.repository.model.paged.ITankPagedRepository;
import ubb.dbsm.utils.paging.IPageable;
import ubb.dbsm.utils.paging.Indexable;
import ubb.dbsm.utils.paging.Page;
import ubb.dbsm.utils.paging.Pageable;

import java.util.List;

public class TankPagedDAO extends TankDAO implements ITankPagedRepository {
    private static final Logger logger = LogManager.getLogger(TankPagedDAO.class);

    public Page<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer, IPageable pageable) {
        logger.debug("findByNameAndManufacturer with Paging with name={} and manufacturer={}", name, manufacturer);
        int lastId = pageable.getPageNumber();

        try (EntityManager em = emf.createEntityManager()) {
            List<Tank> tankList = em.createQuery("SELECT t FROM Tank t JOIN t.manufacturer m WHERE LOWER(t.name) LIKE LOWER(:name) AND m = :manufacturer AND t.id > :id ORDER BY t.id", Tank.class)
                    .setParameter("name", "%" + name + "%")
                    .setParameter("manufacturer", manufacturer)
                    .setParameter("id", lastId)
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();

            if (!tankList.isEmpty() && pageable instanceof Indexable indexable) {
                indexable.setNextId(tankList.getLast().getId());
            }

            long totalItems = em.createQuery("SELECT COUNT(t) FROM Tank t JOIN t.manufacturer m WHERE LOWER(t.name) LIKE LOWER(:name) AND m = :manufacturer", Long.class)
                    .setParameter("name", "%" + name + "%")
                    .setParameter("manufacturer", manufacturer)
                    .getSingleResult();

            logger.info("Found {} tanks with name like '{}' for manufacturer {}", tankList.size(), name, manufacturer);

            return new Page<>(tankList, pageable, totalItems);
        } catch (Exception e) {
            logger.error("Failed in findByNameAndManufacturer with Paging with name={} and manufacturer={}", name, manufacturer, e);
            throw e;
        }
    }

    @Override
    public Page<Tank> getPage(IPageable pageable) {
        return null;
    }
}
