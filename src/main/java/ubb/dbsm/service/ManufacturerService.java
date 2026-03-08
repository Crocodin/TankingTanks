package ubb.dbsm.service;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.repository.DBRepository.ManufacturerDAO;

import java.util.Optional;

public class ManufacturerService extends AbstractService<Integer, Manufacturer> {
    public ManufacturerService() {
        this.repository = new ManufacturerDAO();
    }

    public Optional<Manufacturer> findByName(String name) {
        return ((ManufacturerDAO) this.repository).findByName(name);
    }
}
