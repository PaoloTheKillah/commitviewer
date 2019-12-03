package pchila.commitviewer.core;

public class CommitSourceException extends Exception {

    public CommitSourceException(String message, Throwable cause) {
        super(message, cause);
    }


    public CommitSourceException(String message) {
        super(message);
    }
}
