package pchila.commitviewer.core;

public class RepositoryNotAvailableException extends CommitSourceException {
    public RepositoryNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryNotAvailableException(String message) {
        super(message);
    }
}
