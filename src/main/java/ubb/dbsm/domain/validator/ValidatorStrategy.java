package ubb.dbsm.domain.validator;

public interface ValidatorStrategy<T> {
    boolean validate(T object);
}
