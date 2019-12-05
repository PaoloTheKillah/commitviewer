package pchila.commitviewer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import pchila.commitviewer.api.APICommitViewer;
import pchila.commitviewer.core.CommitSourceException;
import pchila.commitviewer.git.GitSource;
import picocli.CommandLine;

import java.net.URL;

import static picocli.CommandLine.*;


@Command(name = "commitviewer", mixinStandardHelpOptions = true)
public class MainClass implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(MainClass.class);

    @Unmatched
    String[] args = {};

    public static void main(String[] args) {
        new CommandLine(new MainClass()).execute(args);
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Please specify a subcommand");
    }

    @Command
    public void cli(@Parameters(paramLabel = "<repository URL>") URL repoLocation) {
        logger.info("Called with repository {}", repoLocation);
        var src = new GitSource();
        try {
            src.readCommits(repoLocation, 1, 100).forEach(commit -> System.out.println(commit));
            logger.info("Call complete.", repoLocation);
        } catch (CommitSourceException cse) {
            logger.error("Error displaying commits", cse);
            System.err.println("Error retrieving commits. Refer to logs for details.");
            System.exit(-1);
        }
    }

    @Command
    public void server() {
        SpringApplication.run(APICommitViewer.class, args);
    }
}
