package pchila.commitreader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pchila.commitreader.core.CommitSource;
import pchila.commitreader.core.CommitSourceException;
import pchila.commitreader.gitcli.GitCommitSource;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "commitreader",
        mixinStandardHelpOptions = true,
        description = "Retrieves and print a list of commits from a git repository")
public class CommitReader implements Callable<Integer> {
    private static final Logger LOGGER = LogManager.getLogger(CommitReader.class);

    @CommandLine.Parameters(paramLabel = "REPO", description = "Location of git repository")
    private String repoLocation;

    public static void main(String[] args) {
        System.exit(new CommandLine(new CommitReader()).execute(args));
    }

    @Override
    public Integer call() throws Exception {
        int result;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Called with repository {}", this.repoLocation);
        }

        CommitSource src = new GitCommitSource(this.repoLocation);
        try {
            src.readCommits().forEach(c -> System.out.println(c));
            result = 0;
        } catch (CommitSourceException cse) {
            LOGGER.error("Error retrieving commits from {}", cse);
            System.err.println("Error retrieving commits. Refer to logs for details.");
            result = -1;
        }
        return result;
    }
}
