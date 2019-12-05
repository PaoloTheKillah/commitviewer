package pchila.commitviewer.git;

import org.junit.Test;
import pchila.commitviewer.core.Commit;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.junit.Assert.assertEquals;

public class GitOutputToCommitMapperTest {


    @Test
    public void parseCommit() {

        var now = ZonedDateTime.now();

        var sampleCommitData = Map.ofEntries(
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.HASH.ordinal(), "hash"),
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.AUTHOR_NAME.ordinal(), "Someone Something"),
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.AUTHOR_MAIL.ordinal(), "foo@bar.baz"),
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.AUTHOR_DATE.ordinal(), now.format(DateTimeFormatter.ISO_DATE_TIME)),
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.COMMITTER_NAME.ordinal(), "blah"),
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.COMMITTER_MAIL.ordinal(), "abc@def.xyz"),
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.COMMITTER_DATE.ordinal(), now.format(DateTimeFormatter.ISO_DATE_TIME)),
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.MESSAGE.ordinal(), "subject line\n\nbody line1\nline2\nline3"),
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.REFS.ordinal(), "ref1, ref2"),
                entry(GitOutputToCommitMapper.FormatStringSpecifiers.PARENT_COMMITS.ordinal(), "commit1 commit2")
        );

        var rawCommitString = Stream.of(GitOutputToCommitMapper.FormatStringSpecifiers.values())
                .map(v -> sampleCommitData.get(v.ordinal()))
                .collect(Collectors.joining("\0"));

        var expectedCommit = new Commit(
                sampleCommitData.get(GitOutputToCommitMapper.FormatStringSpecifiers.HASH.ordinal()),
                new Commit.Developer(
                        sampleCommitData.get(GitOutputToCommitMapper.FormatStringSpecifiers.AUTHOR_NAME.ordinal()),
                        sampleCommitData.get(GitOutputToCommitMapper.FormatStringSpecifiers.AUTHOR_MAIL.ordinal()),
                        now
                ),
                new Commit.Developer(
                        sampleCommitData.get(GitOutputToCommitMapper.FormatStringSpecifiers.COMMITTER_NAME.ordinal()),
                        sampleCommitData.get(GitOutputToCommitMapper.FormatStringSpecifiers.COMMITTER_MAIL.ordinal()),
                        now
                ),
                sampleCommitData.get(GitOutputToCommitMapper.FormatStringSpecifiers.MESSAGE.ordinal()),
                sampleCommitData.get(GitOutputToCommitMapper.FormatStringSpecifiers.REFS.ordinal()).split(", "),
                sampleCommitData.get(GitOutputToCommitMapper.FormatStringSpecifiers.PARENT_COMMITS.ordinal()).split(" ")
        );

        var actualCommit = new GitOutputToCommitMapper().parseCommit(rawCommitString);

        assertEquals(expectedCommit, actualCommit);
    }
}