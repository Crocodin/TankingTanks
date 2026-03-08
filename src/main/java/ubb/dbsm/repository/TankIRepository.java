package ubb.dbsm.repository;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;

import java.util.List;

public interface TankIRepository extends IRepository<Integer, Tank> {
    List<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer);
}
