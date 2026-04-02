package ubb.dbsm.service;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.domain.validator.ValidatorStrategy;
import ubb.dbsm.repository.TankIRepository;

import java.util.ArrayList;
import java.util.List;

public class TankService extends AbstractService<Integer, Tank> {
    public TankService(TankIRepository tankIRepository, ValidatorStrategy<Tank> validatorStrategy) {
        this.repository = tankIRepository;
        this.validatorStrategy = validatorStrategy;
    }

    public List<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer) {
        if (manufacturer == null) return new ArrayList<>();
        return ((TankIRepository) repository).findByNameAndManufacturer(name, manufacturer);
    }

    public List<Tank> findByManufacturer(Manufacturer manufacturer) {
        return findByNameAndManufacturer("", manufacturer);
    }
}
