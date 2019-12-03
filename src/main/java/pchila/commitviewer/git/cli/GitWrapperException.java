package pchila.commitviewer.git.cli;

public class GitWrapperException extends Exception{

    public GitWrapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitWrapperException(String message) {
        super(message);
    }
}
