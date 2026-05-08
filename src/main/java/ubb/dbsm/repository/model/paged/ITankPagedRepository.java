package ubb.dbsm.repository.model.paged;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.repository.IPageRepository;
import ubb.dbsm.repository.model.ITankRepository;
import ubb.dbsm.utils.paging.IPageable;
import ubb.dbsm.utils.paging.Page;

public interface ITankPagedRepository extends ITankRepository, IPageRepository<Integer, Tank> {
    Page<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer, IPageable pageable);
}
