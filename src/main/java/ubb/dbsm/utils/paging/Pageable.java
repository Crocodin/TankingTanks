package ubb.dbsm.utils.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pageable {
    private int pageNumber;
    private int pageSize;

    public void incrementPageNumber() {
        this.pageNumber++;
    }

    public void decrementPageNumber() {
        if (pageNumber > 0) this.pageNumber--;
    }
}
