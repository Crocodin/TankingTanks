package ubb.dbsm.repository.model.paged;

import ubb.dbsm.domain.Tank;
import ubb.dbsm.repository.IPageRepository;
import ubb.dbsm.repository.model.ITankRepository;

public interface ITankPagedRepository extends ITankRepository, IPageRepository<Integer, Tank> {
}
