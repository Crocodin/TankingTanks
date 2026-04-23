package ubb.dbsm.utils.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

@Setter
public class Indexable implements IPageable{
    private int currentId;
    private int nextId;
    @Getter
    private Stack<Integer> previousId = new Stack<>();
    private int pageSize;

    @Override
    public int getPageNumber() {
        return currentId;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void increment() {
        this.currentId = nextId;
    }

    @Override
    public void decrement() {
        this.currentId = previousId.pop();
    }

    public Indexable(int pageSize) {
        this.pageSize = pageSize;
        this.currentId = 0;
    }
}
