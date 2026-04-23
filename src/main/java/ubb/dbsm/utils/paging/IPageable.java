package ubb.dbsm.utils.paging;

public interface IPageable {
    public int getPageNumber();
    public int getPageSize();
    public void increment();
    public void decrement();
}
