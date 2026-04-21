package ubb.dbsm.repository;

import ubb.dbsm.utils.paging.Page;
import ubb.dbsm.utils.paging.Pageable;

public interface IPageRepository<ID, T> extends IRepository<ID, T> {
    Page<T> getPage(Pageable pageable);
}
