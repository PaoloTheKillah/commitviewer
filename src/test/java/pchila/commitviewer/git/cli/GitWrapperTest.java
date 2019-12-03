package pchila.commitviewer.git.cli;

import org.junit.Ignore;
import org.junit.Test;
import pchila.commitviewer.core.CommitSourceException;
import pchila.commitviewer.git.GitSource;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class GitWrapperTest {

    private static final String EMPTY_REPO_URL = "https://github.com/PaoloTheKillah/TestEmptyRepository";
    private static final String NONEXISTENT_OR_PRIVATE_REPO_URL = "https://github.com/PaoloTheKillah/DoesNotExist";
    private static final String SAMPLE_REPO_URL = "https://github.com/PaoloTheKillah/SampleRepo";


    @Test
    public void fetchEmptyRepository() throws GitWrapperException {
        var wrapper = new GitWrapper(EMPTY_REPO_URL);
        wrapper.fetch();
    }

    @Test(expected = GitWrapperException.class)
    public void fetchNonExistingRepositoryShouldThrow() throws GitWrapperException {
        var wrapper = new GitWrapper(NONEXISTENT_OR_PRIVATE_REPO_URL);
        wrapper.fetch();
    }

    @Test(expected = GitWrapperException.class)
    @Ignore("With the current Scanner implementation we cant detect this")
    public void logEmptyRepositoryShouldThrow() throws GitWrapperException {
        var wrapper = new GitWrapper(EMPTY_REPO_URL);
        wrapper.fetch();
        wrapper.log("%H", "\n");
    }

    @Test
    public void logSampleRepositoryAndCountCommits() throws GitWrapperException, CommitSourceException {
        var wrapper = new GitWrapper(SAMPLE_REPO_URL);
        wrapper.fetch();
        var commitCount = wrapper.log("%H", "\n").collect(Collectors.counting());
        assertEquals(3, (long)commitCount);

        // Make sure that we can use the commands issued when displaying commits

        var source = new GitSource(wrapper);
        var readCommits = source.readCommits().collect(Collectors.counting());
        assertEquals((long)commitCount, (long)readCommits);
    }
}