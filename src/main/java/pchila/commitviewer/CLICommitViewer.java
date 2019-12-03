package pchila.commitviewer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pchila.commitviewer.core.CommitSourceException;
import pchila.commitviewer.git.GitSource;
import pchila.commitviewer.git.cli.GitWrapper;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "commitviewer",
        mixinStandardHelpOptions = true,
        description = "Retrieves and print a list of commits from a git repository")
public class CLICommitViewer implements Callable<Integer> {
    private static final Logger LOGGER = LogManager.getLogger(CLICommitViewer.class);

    @CommandLine.Parameters(paramLabel = "REPO", description = "Location of git repository")
    private String repoLocation;

    public static void main(String[] args) {
        System.exit(new CommandLine(new CLICommitViewer()).execute(args));
    }

    @Override
    public Integer call() throws Exception {

        LOGGER.info("Called with repository {}", this.repoLocation);
        var gitWrapper = new GitWrapper(this.repoLocation);
        var src = new GitSource(gitWrapper);
        try {
            src.readCommits().forEach(commit -> System.out.println(commit));
            LOGGER.info("Call complete.", this.repoLocation);
            return 0;
        } catch (CommitSourceException cse) {
            LOGGER.error("Error displaying commits", cse);
            System.err.println("Error retrieving commits. Refer to logs for details.");
            return -1;
        }
    }
}
