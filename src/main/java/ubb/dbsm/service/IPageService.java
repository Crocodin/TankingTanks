package ubb.dbsm.service;

import ubb.dbsm.utils.paging.Page;
import ubb.dbsm.utils.paging.IPageable;

public interface IPageService<T> {
    Page<T> getPage(IPageable pageable);
}
