package pchila.commitviewer.git.cli;

import org.junit.Ignore;
import org.junit.Test;
import pchila.commitviewer.core.CommitReadException;
import pchila.commitviewer.core.CommitSourceException;
import pchila.commitviewer.core.RepositoryNotAvailableException;
import pchila.commitviewer.git.GitSource;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class GitWrapperTest {

    private static final String EMPTY_REPO_URL = "https://github.com/PaoloTheKillah/TestEmptyRepository";
    private static final String NONEXISTENT_OR_PRIVATE_REPO_URL = "https://github.com/PaoloTheKillah/DoesNotExist";
    private static final String SAMPLE_REPO_URL = "https://github.com/PaoloTheKillah/SampleRepo";


    @Test
    public void fetchEmptyRepository() throws CommitSourceException, MalformedURLException {
        var wrapper = new GitWrapper();
        wrapper.fetch(new URL(EMPTY_REPO_URL));
    }

    @Test(expected = RepositoryNotAvailableException.class)
    public void fetchNonExistingRepositoryShouldThrow() throws CommitSourceException, MalformedURLException {
        var wrapper = new GitWrapper();
        wrapper.fetch(new URL(NONEXISTENT_OR_PRIVATE_REPO_URL));
    }

    @Test(expected = CommitReadException.class)
    @Ignore("With the current Scanner implementation we can't detect this")
    public void logEmptyRepositoryShouldThrow() throws CommitSourceException, MalformedURLException {
        var wrapper = new GitWrapper();
        wrapper.fetch(new URL(EMPTY_REPO_URL));
        wrapper.log("%H", "\n", 1, 100);
    }

    @Test
    public void logSampleRepositoryAndCountCommits() throws  CommitSourceException, MalformedURLException {
        var wrapper = new GitWrapper();
        wrapper.fetch(new URL(SAMPLE_REPO_URL));
        var commitCount = wrapper.log("%H", "\n", 1, 100).count();
        assertEquals(3, (long)commitCount);

        // Make sure that we can use the commands issued when displaying commits

        var source = new GitSource();
        var readCommits = source.readCommits(new URL(SAMPLE_REPO_URL), 1, 100).count();
        assertEquals((long)commitCount, (long)readCommits);
    }
}