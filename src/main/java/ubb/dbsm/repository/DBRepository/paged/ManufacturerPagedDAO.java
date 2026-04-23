package ubb.dbsm.repository.DBRepository.paged;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.repository.DBRepository.ManufacturerDAO;
import ubb.dbsm.repository.model.paged.IManufacturerPagedRepository;
import ubb.dbsm.utils.paging.IPageable;
import ubb.dbsm.utils.paging.Page;

import java.util.List;

@Slf4j
@Primary
@Repository
public class ManufacturerPagedDAO extends ManufacturerDAO implements IManufacturerPagedRepository {
    
    @Override
    public Page<Manufacturer> getPage(IPageable pageable) {
        log.debug("enter ManufacturerPagedDAO.getPage() with page  number {} and size {}",  pageable.getPageNumber(), pageable.getPageSize());
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();

        List<Manufacturer> manufacturer = em.createQuery("SELECT m FROM Manufacturer m ORDER BY m.id", Manufacturer.class)
                .setFirstResult(offset)
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long totalItems = em.createQuery("SELECT COUNT(m) FROM Manufacturer m", Long.class).getSingleResult();


        log.debug("exiting getPage() with {} items", manufacturer.size());
        return new Page<>(manufacturer, pageable, totalItems);
    }
}
