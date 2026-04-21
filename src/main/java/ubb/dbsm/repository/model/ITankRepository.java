package ubb.dbsm.repository.model;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.repository.IRepository;

import java.util.List;

public interface ITankRepository extends IRepository<Integer, Tank> {
    List<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer);
}
