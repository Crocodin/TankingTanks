package ubb.dbsm.service;

import ubb.dbsm.utils.paging.Page;
import ubb.dbsm.utils.paging.Pageable;

public interface IPageService<T> {
    Page<T> getPage(Pageable pageable);
}
