package pchila.commitviewer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.stream.Stream;

@FunctionalInterface
public interface CommitSource {

    Logger logger = LoggerFactory.getLogger(CommitSource.class);

    Stream<Commit> readCommits(URL repositoryUrl, int page, int pageSize) throws CommitSourceException;

    default CommitSource addFallback(CommitSource source) {
        return ((repositoryUrl, page, pageSize) -> {
            try {
                return readCommits(repositoryUrl, page, pageSize);
            } catch(CommitSourceException cse) {
                logger.warn("Error  reading commit using " + this.getClass().getCanonicalName() + " Falling back to "
                        + source.getClass().getCanonicalName(), cse);
                return source.readCommits(repositoryUrl, page, pageSize);
            }
        });
    }
}
