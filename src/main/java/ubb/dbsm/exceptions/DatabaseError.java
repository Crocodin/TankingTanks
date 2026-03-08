package ubb.dbsm.exceptions;

public class DatabaseError extends RuntimeException {
    public DatabaseError(String message) {
        super(message);
    }
    public DatabaseError(String message, Throwable cause) {
        super(message, cause);
    }
}
