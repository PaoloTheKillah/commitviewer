package pchila.commitreader.gitcli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pchila.commitreader.core.Commit;
import pchila.commitreader.core.CommitSource;
import pchila.commitreader.core.CommitSourceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GitCommitSource implements CommitSource {

    private static final Logger LOGGER = LogManager.getLogger(GitCommitSource.class);

    private static final String GIT_FORMAT_ARG = "--format=" + StringToCommitMapper.format();

    private static final int GIT_PROCESS_WAIT_TIMEOUT_MS = 500;

    private ProcessBuilder gitProcessBuilder;

    public GitCommitSource() {
        this("");
    }

    public GitCommitSource(String repositoryPath) {
        this.gitProcessBuilder = new ProcessBuilder("git", "--no-pager", "-C", repositoryPath, "log", GIT_FORMAT_ARG);
    }

    @Override
    public Stream<Commit> readCommits() throws CommitSourceException {
        Process gitProcess = null;

        try {
            gitProcess = this.gitProcessBuilder.start();
            LOGGER.debug("Launched git process with pid {} with arguments {}", gitProcess.pid(), this.gitProcessBuilder.command());
        } catch (IOException e) {
            throw new CommitSourceException("Unable to start git process.", e);
        }

        StringToCommitMapper mapper = new StringToCommitMapper();

        try (Stream<String> raw_output =
                     new BufferedReader(
                             new InputStreamReader(
                                     gitProcess.getInputStream()
                             )
                     ).lines()
        ) {
            return mapper.streamCommits(raw_output.collect(Collectors.joining())).map(mapper::map);
        } finally {
            try {
                if (!gitProcess.waitFor(GIT_PROCESS_WAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                    LOGGER.warn("Git process {} did not terminate. Terminating forcefully.", gitProcess.pid());
                    gitProcess.destroyForcibly();
                }

                if (gitProcess.exitValue() != 0) {
                    // collect stderr output useful and attach it to the exception
                    String stderr_output = new BufferedReader(new InputStreamReader(gitProcess.getErrorStream()))
                            .lines()
                            .collect(Collectors.joining());
                    throw new CommitSourceException(
                            String.format("Git process {} terminated abnormally: {}",
                                    gitProcess.pid(),
                                    stderr_output)
                    );
                }

            } catch (InterruptedException e) {
                throw new CommitSourceException("Thread interrupted while waiting for git process to terminate", e);
            }


        }
    }

    private static class StringToCommitMapper {
        private static final String FIELD_SEPARATOR = "\0";
        private static final char RS_CHAR = 0x1e;
        private static final String RECORD_SEPARATOR = "" + RS_CHAR;
        private static final String RECORD_SEPARATOR_PLACEHOLDER = "%x1E";
        private static final String FIELD_SEPARATOR_PLACEHOLDER = "%x00";

        public static String format() {
            return Stream.of(FormatStringSpecifiers.values())
                    .map(field -> field.placeholder())
                    .map(str -> str + FIELD_SEPARATOR_PLACEHOLDER)
                    .collect(Collectors.joining()) + RECORD_SEPARATOR_PLACEHOLDER;
        }

        public Stream<String> streamCommits(String text) {
            return Arrays.stream(text.split(RECORD_SEPARATOR));
        }

        public Commit map(String rawCommit) {
            LOGGER.debug("Raw commit:{}", rawCommit);
            String[] splitCommit = rawCommit.split(FIELD_SEPARATOR, -1);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Split commit:{}", Arrays.toString(splitCommit));
            }
            return new Commit(splitCommit[FormatStringSpecifiers.HASH.ordinal()],
                    new Commit.Author(
                            splitCommit[FormatStringSpecifiers.AUTHOR_NAME.ordinal()],
                            splitCommit[FormatStringSpecifiers.AUTHOR_MAIL.ordinal()]),
                    new Commit.Message( splitCommit[FormatStringSpecifiers.SUBJECT.ordinal()],
                            splitCommit[FormatStringSpecifiers.BODY.ordinal()]
                    ),
                    ZonedDateTime.parse(splitCommit[FormatStringSpecifiers.AUTHOR_DATE.ordinal()]),
                    ZonedDateTime.parse(splitCommit[FormatStringSpecifiers.COMMITTER_DATE.ordinal()])
            );
        }

        private enum FormatStringSpecifiers {
            HASH("%H"),
            AUTHOR_NAME("%aN"),
            AUTHOR_MAIL("%aE"),
            AUTHOR_DATE("%aI"),
            COMMITTER_DATE("%cI"),
            SUBJECT("%s"),
            BODY("%b");

            private final String placeholder;

            FormatStringSpecifiers(String placeholder) {
                this.placeholder = placeholder;
            }

            public String placeholder() {
                return this.placeholder;
            }
        }

    }


}
