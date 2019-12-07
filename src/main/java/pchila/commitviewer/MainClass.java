package pchila.commitviewer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import pchila.commitviewer.api.APICommitViewer;
import pchila.commitviewer.core.CommitSourceException;
import pchila.commitviewer.git.GitSource;
import pchila.commitviewer.github.GitHubSource;
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
    public void cli(@Parameters(paramLabel = "<repository URL>") URL repoLocation,
                    @Option(names = {"-p", "--page"}, paramLabel = "PAGE", defaultValue = "1") int page,
                    @Option(names = {"-s", "--page-size"}, paramLabel = "PAGESIZE", defaultValue = "100") int pageSize) {
        logger.info("Called with repository {}", repoLocation);
        var src = new GitHubSource().addFallback(new GitSource());
        try {
            src.readCommits(repoLocation, page, pageSize).forEach(commit -> System.out.println(commit));
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
