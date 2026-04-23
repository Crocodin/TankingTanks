package ubb.dbsm.repository;

import ubb.dbsm.utils.paging.IPageable;
import ubb.dbsm.utils.paging.Page;

public interface IPageRepository<ID, T> extends IRepository<ID, T> {
    Page<T> getPage(IPageable pageable);
}
