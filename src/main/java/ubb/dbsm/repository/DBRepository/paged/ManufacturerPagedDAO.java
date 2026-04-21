package ubb.dbsm.repository.DBRepository.paged;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.repository.DBRepository.ManufacturerDAO;
import ubb.dbsm.repository.IPageRepository;
import ubb.dbsm.repository.model.paged.IManufacturerPagedRepository;
import ubb.dbsm.utils.paging.Page;
import ubb.dbsm.utils.paging.Pageable;

import java.util.List;
import java.util.Optional;

public class ManufacturerPagedDAO extends ManufacturerDAO implements IManufacturerPagedRepository {
    private static final Logger logger = LogManager.getLogger(ManufacturerPagedDAO.class);

    @Override
    public Page<Manufacturer> getPage(Pageable pageable) {
        logger.debug("enter ManufacturerPagedDAO.getPage() with page  number {} and size {}",  pageable.getPageNumber(), pageable.getPageSize());
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();

        try (EntityManager em = emf.createEntityManager()) {
            List<Manufacturer> manufacturer = em.createQuery("SELECT m FROM Manufacturer m ORDER BY m.id", Manufacturer.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();

            long totalItems = em.createQuery("SELECT COUNT(m) FROM Manufacturer m", Long.class).getSingleResult();


            logger.debug("exiting getPage() with {} items", manufacturer.size());
            return new Page<>(manufacturer, pageable, totalItems);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}
