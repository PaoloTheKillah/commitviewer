package pchila.commitviewer.git;

import pchila.commitviewer.core.Commit;
import pchila.commitviewer.core.CommitSource;
import pchila.commitviewer.core.CommitSourceException;
import pchila.commitviewer.git.cli.GitWrapper;
import pchila.commitviewer.git.cli.GitWrapperException;

import java.net.URL;
import java.util.stream.Stream;

public class GitSource implements CommitSource {

    private static final String GIT_FORMAT_ARG =  GitOutputToCommitMapper.format();

    @Override
    public Stream<Commit> readCommits(URL repositoryUrl, int page, int pageSize) throws CommitSourceException {
        {
            GitWrapper wrapper = new GitWrapper();
            GitOutputToCommitMapper mapper = new GitOutputToCommitMapper();

            wrapper.fetch(repositoryUrl);
            return mapper.streamCommits(wrapper.log(GIT_FORMAT_ARG, GitOutputToCommitMapper.RECORD_SEPARATOR, page, pageSize));

        }
    }


}
