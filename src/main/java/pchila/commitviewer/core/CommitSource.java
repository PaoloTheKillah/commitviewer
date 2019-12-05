package pchila.commitviewer.core;

import java.net.URL;
import java.util.stream.Stream;

public interface CommitSource {

    Stream<Commit> readCommits(URL repositoryUrl, int page, int pageSize) throws CommitSourceException;

}
