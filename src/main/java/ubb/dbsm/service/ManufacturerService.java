package ubb.dbsm.service;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.repository.DBRepository.ManufacturerDAO;
import ubb.dbsm.repository.IRepository;

import java.util.Optional;

public class ManufacturerService extends AbstractService<Integer, Manufacturer> {

    public ManufacturerService(IRepository<Integer, Manufacturer> repository) {
        this.repository = repository;
    }

    public Optional<Manufacturer> findByName(String name) {
        logger.info("Finding Manufacturer by name: {}", name);
        return ((ManufacturerDAO) this.repository).findByName(name);
    }
}
