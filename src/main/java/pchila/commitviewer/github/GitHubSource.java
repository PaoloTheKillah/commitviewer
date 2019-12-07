package pchila.commitviewer.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pchila.commitviewer.core.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

public class GitHubSource implements CommitSource {

    private static final Logger logger = LoggerFactory.getLogger(GitHubSource.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private static <R> Commit mapToCoreCommit(GithubCommit githubCommit) {

        GithubCommit.Commit innerCommit = githubCommit.getCommit();
        GithubCommit.Developer author = innerCommit.getAuthor();
        GithubCommit.Developer committer = innerCommit.getCommitter();

        return new Commit(
                githubCommit.getSha(),
                new Commit.Developer(
                        author.getName(),
                        author.getEmail(),
                        ZonedDateTime.parse(author.getDate())
                ),
                new Commit.Developer(
                        committer.getName(),
                        committer.getEmail(),
                        ZonedDateTime.parse(committer.getDate())
                ),
                innerCommit.getMessage(),
                new String[]{},
                Stream.of(githubCommit.getParents()).map(p -> p.getSha()).toArray(String[]::new)
        );

    }

    private static <U> List<GithubCommit> deserializeBody(String s) {
        try {
            return Arrays.asList(mapper.readValue(s, GithubCommit[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing body", e);
        }
    }

    private static <U> HttpResponse<String> checkStatusCode(HttpResponse<String> stringHttpResponse) {
        //TODO we should include the status code and message from github api in the exception message
        int statusCode = stringHttpResponse.statusCode();
        if (statusCode < 200 || statusCode >= 400) {
            throw new RuntimeException("Response from github has code " + statusCode);
        }
        return stringHttpResponse;
    }

    @Override
    public Stream<Commit> readCommits(URL repositoryUrl, int page, int pageSize) throws CommitSourceException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(30)).build();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https",
                            "api.github.com",
                            mountPath(repositoryUrl.getPath()),
                            "page=" + page + "&per_page=" + pageSize,
                            null
                    ))
                    .timeout(Duration.ofMinutes(10))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            logger.error("Github request: {}", request);

            List<GithubCommit> gitHubCommits = new ArrayList<>();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(GitHubSource::checkStatusCode)
                    .thenApply(HttpResponse::body)
                    .thenApply(GitHubSource::deserializeBody)
                    .thenAccept(cl -> gitHubCommits.addAll(cl)).join();
            return gitHubCommits.stream().map(GitHubSource::mapToCoreCommit);
        } catch (URISyntaxException e) {
            throw new CommitReadException(String.format("Error composing github API URL for repository {}", repositoryUrl), e);
        } catch (CompletionException e) {
            throw new RepositoryNotAvailableException("Error consuming github response", e);
        }
    }

    private String mountPath(String path) {

        var builder = new StringBuilder("/repos");
        builder.append(path);
        if (!path.endsWith("/"))
            builder.append("/");
        builder.append("commits");

        return builder.toString();

    }
}
