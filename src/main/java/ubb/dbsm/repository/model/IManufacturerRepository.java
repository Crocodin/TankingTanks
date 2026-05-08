package ubb.dbsm.repository.model;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.repository.IRepository;

import java.util.Optional;

public interface IManufacturerRepository extends IRepository<Integer, Manufacturer> {
    Optional<Manufacturer> findByName(String name);
}
