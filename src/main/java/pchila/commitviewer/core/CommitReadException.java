package pchila.commitviewer.core;

public class CommitReadException extends CommitSourceException {
    public CommitReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommitReadException(String message) {
        super(message);
    }
}
