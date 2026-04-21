package ubb.dbsm.repository.model.paged;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.repository.IPageRepository;
import ubb.dbsm.repository.model.IManufacturerRepository;

public interface IManufacturerPagedRepository extends IManufacturerRepository, IPageRepository<Integer, Manufacturer> {
}
