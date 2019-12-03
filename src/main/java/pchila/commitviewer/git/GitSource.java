package pchila.commitviewer.git;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pchila.commitviewer.core.Commit;
import pchila.commitviewer.core.CommitSource;
import pchila.commitviewer.core.CommitSourceException;
import pchila.commitviewer.git.cli.GitWrapper;
import pchila.commitviewer.git.cli.GitWrapperException;

import java.util.stream.Stream;

public class GitSource implements CommitSource {

    private static final Logger LOGGER = LogManager.getLogger(GitSource.class);

    private static final String GIT_FORMAT_ARG =  GitOutputToCommitMapper.format();

    private GitWrapper gitWrapper;

    public GitSource(GitWrapper gitWrapper) {
        this.gitWrapper = gitWrapper;
    }

    @Override
    public Stream<Commit> readCommits() throws CommitSourceException {
        {
            GitOutputToCommitMapper mapper = new GitOutputToCommitMapper();
            try {
                this.gitWrapper.fetch();
                return mapper.streamCommits(this.gitWrapper.log(GIT_FORMAT_ARG, GitOutputToCommitMapper.RECORD_SEPARATOR));
            } catch (GitWrapperException e) {
                throw new CommitSourceException("Error retrieving commit log.", e);
            }
        }
    }


}
