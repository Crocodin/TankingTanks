package ubb.dbsm.service.model;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.repository.DBRepository.ManufacturerDAO;
import ubb.dbsm.repository.IPageRepository;
import ubb.dbsm.repository.IRepository;
import ubb.dbsm.repository.model.paged.IManufacturerPagedRepository;
import ubb.dbsm.service.AbstractService;
import ubb.dbsm.utils.paging.IPageable;
import ubb.dbsm.utils.paging.Page;
import ubb.dbsm.utils.paging.Pageable;

import java.util.Optional;

public class ManufacturerService extends AbstractService<Integer, Manufacturer, IManufacturerPagedRepository> implements IPageRepository<Integer, Manufacturer> {

    public ManufacturerService(IManufacturerPagedRepository repository) {
        this.repository = repository;
    }

    public Optional<Manufacturer> findByName(String name) {
        logger.debug("Finding Manufacturer by name: {}", name);
        return this.repository.findByName(name);
    }

    @Override
    public Page<Manufacturer> getPage(IPageable pageable) {
        return this.repository.getPage(pageable);
    }
}
