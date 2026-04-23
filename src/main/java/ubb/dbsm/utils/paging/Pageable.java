package ubb.dbsm.utils.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pageable implements IPageable {
    private int pageNumber;
    private int pageSize;

    @Override
    public void increment() {
        this.pageNumber++;
    }

    @Override
    public void decrement() {
        if (pageNumber > 0) this.pageNumber--;
    }
}
