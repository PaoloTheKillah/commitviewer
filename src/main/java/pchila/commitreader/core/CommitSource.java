package pchila.commitreader.core;

import java.util.stream.Stream;

public interface CommitSource {

    Stream<Commit> readCommits() throws CommitSourceException;

}
