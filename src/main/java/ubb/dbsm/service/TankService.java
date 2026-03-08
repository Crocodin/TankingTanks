package ubb.dbsm.service;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.domain.validator.TankValidator;
import ubb.dbsm.repository.DBRepository.TankDAO;
import ubb.dbsm.repository.TankIRepository;

import java.util.ArrayList;
import java.util.List;

public class TankService extends AbstractService<Integer, Tank> {
    public TankService() {
        this.repository = new TankDAO();
        this.validatorStrategy = new TankValidator();
    }

    public List<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer) {
        if (manufacturer == null) return new ArrayList<>();
        return ((TankIRepository) repository).findByNameAndManufacturer(name, manufacturer);
    }

    public List<Tank> findByManufacturer(Manufacturer manufacturer) {
        return findByNameAndManufacturer("", manufacturer);
    }
}
