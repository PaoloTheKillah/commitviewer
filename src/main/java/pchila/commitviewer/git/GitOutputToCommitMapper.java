package pchila.commitviewer.git;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pchila.commitviewer.core.Commit;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class GitOutputToCommitMapper {

    private static final Logger logger = LoggerFactory.getLogger(GitOutputToCommitMapper.class);
    private static final String FIELD_SEPARATOR = "\0";
    private static final char RS_CHAR = 0x1e;
    public static final String RECORD_SEPARATOR = new String(new char[]{RS_CHAR, '\n'});
    private static final String RECORD_SEPARATOR_PLACEHOLDER = "%x1E";
    private static final String FIELD_SEPARATOR_PLACEHOLDER = "%x00";

    public static String format() {
        return Stream.of(FormatStringSpecifiers.values())
                .map(field -> field.placeholder())
                .collect(Collectors.joining(FIELD_SEPARATOR_PLACEHOLDER)) + RECORD_SEPARATOR_PLACEHOLDER;
    }

    public Stream<Commit> streamCommits(Stream<String> gitLogOutput) {
        return gitLogOutput.map(commitString -> parseCommit(commitString));
    }

    Commit parseCommit(String rawCommit) {
        logger.debug("Raw commit:{}", rawCommit);
        String[] splitCommit = rawCommit.split(FIELD_SEPARATOR, -1);
        if (logger.isDebugEnabled()) {
            logger.debug("Split commit:{}", Arrays.toString(splitCommit));
        }
        return new Commit(splitCommit[FormatStringSpecifiers.HASH.ordinal()],
                new Commit.Developer(
                        splitCommit[FormatStringSpecifiers.AUTHOR_NAME.ordinal()],
                        splitCommit[FormatStringSpecifiers.AUTHOR_MAIL.ordinal()],
                        ZonedDateTime.parse(splitCommit[FormatStringSpecifiers.AUTHOR_DATE.ordinal()])
                ),
                new Commit.Developer(
                        splitCommit[FormatStringSpecifiers.COMMITTER_NAME.ordinal()],
                        splitCommit[FormatStringSpecifiers.COMMITTER_MAIL.ordinal()],
                        ZonedDateTime.parse(splitCommit[FormatStringSpecifiers.COMMITTER_DATE.ordinal()])
                ),
                splitCommit[FormatStringSpecifiers.MESSAGE.ordinal()],
                splitCommit[FormatStringSpecifiers.REFS.ordinal()].split(", "),
                splitCommit[FormatStringSpecifiers.PARENT_COMMITS.ordinal()].split(" ")
        );
    }

    enum FormatStringSpecifiers {
        HASH("%H"),
        AUTHOR_NAME("%aN"),
        AUTHOR_MAIL("%aE"),
        AUTHOR_DATE("%aI"),
        COMMITTER_NAME("%cN"),
        COMMITTER_MAIL("%cE"),
        COMMITTER_DATE("%cI"),
        MESSAGE("%B"),
        REFS("%D"),
        PARENT_COMMITS("%P");

        private final String placeholder;

        FormatStringSpecifiers(String placeholder) {
            this.placeholder = placeholder;
        }

        public String placeholder() {
            return this.placeholder;
        }
    }

}
