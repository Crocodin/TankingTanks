package ubb.dbsm.service.model;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.domain.validator.ValidatorStrategy;
import ubb.dbsm.repository.model.ITankRepository;
import ubb.dbsm.repository.model.paged.ITankPagedRepository;
import ubb.dbsm.service.AbstractService;
import ubb.dbsm.service.IPageService;
import ubb.dbsm.utils.paging.Page;
import ubb.dbsm.utils.paging.Pageable;

import java.util.ArrayList;
import java.util.List;

public class TankService extends AbstractService<Integer, Tank, ITankPagedRepository> implements IPageService<Tank> {
    public TankService(ITankPagedRepository repository, ValidatorStrategy<Tank> validatorStrategy) {
        this.repository = repository;
        this.validatorStrategy = validatorStrategy;
    }

    public List<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer) {
        if (manufacturer == null) return new ArrayList<>();
        return ((ITankRepository) repository).findByNameAndManufacturer(name, manufacturer);
    }

    public List<Tank> findByManufacturer(Manufacturer manufacturer) {
        return findByNameAndManufacturer("", manufacturer);
    }

    @Override
    public Page<Tank> getPage(Pageable pageable) {
        return repository.getPage(pageable);
    }
}
