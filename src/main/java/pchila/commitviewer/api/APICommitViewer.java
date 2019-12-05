package pchila.commitviewer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pchila.commitviewer.core.Commit;
import pchila.commitviewer.core.CommitSourceException;
import pchila.commitviewer.github.GitHubSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringBootApplication
@RestController
public class APICommitViewer {

    private static final Logger logger = LoggerFactory.getLogger(APICommitViewer.class);

    @RequestMapping(value = "viewcommits", method = GET)
    public Stream<Commit> viewCommits(@RequestParam(value = "repo") String repo,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "pageSize", defaultValue = "100") int pageSize) {


        logger.info("Handling request for repo {}, page {}, page_size {}", repo, page, pageSize);
        try {
            return new GitHubSource().readCommits(new URL(repo), page, pageSize);
        } catch (CommitSourceException e) {
            logger.error("Error reading commits", e);
        } catch (MalformedURLException e) {
            logger.error("Malformed URL", e);
        }
        return Stream.empty();
    }
}
