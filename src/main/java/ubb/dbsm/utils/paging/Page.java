package ubb.dbsm.utils.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Page<T> {
    private List<T> itemsOnPage;
    private Pageable pageable;
    private long totalItems;
}
