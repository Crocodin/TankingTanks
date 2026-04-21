package ubb.dbsm.repository.DBRepository.paged;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.repository.DBRepository.TankDAO;
import ubb.dbsm.repository.model.paged.ITankPagedRepository;
import ubb.dbsm.utils.paging.Page;
import ubb.dbsm.utils.paging.Pageable;

import java.util.List;

public class TankPagedDAO extends TankDAO implements ITankPagedRepository {
    private static final Logger logger = LogManager.getLogger(TankPagedDAO.class);

    @Override
    public Page<Tank> getPage(Pageable pageable) {
        logger.debug("enter TankPagedDAO.getPage() with page  number {} and size {}",  pageable.getPageNumber(), pageable.getPageSize());
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();

        try (EntityManager em = emf.createEntityManager()) {
            List<Tank> tanks = em.createQuery("SELECT t FROM Tank t ORDER BY t.id", Tank.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();

            long totalItems = em.createQuery("SELECT COUNT(t) FROM Tank t", Long.class).getSingleResult();


            logger.debug("exiting getPage() with {} items", tanks.size());
            return new Page<>(tanks, pageable, totalItems);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}
