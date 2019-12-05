package pchila.commitviewer.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubCommit {
    private String sha;
    private Commit commit;
    private ParentCommit[] parents;

    public String getSha() {
        return sha;
    }

    public Commit getCommit() {
        return commit;
    }

    public ParentCommit[] getParents() {
        return parents;
    }

    @Override
    public String toString() {
        return "GithubCommit{" +
                "sha='" + sha + '\'' +
                ", commit=" + commit +
                ", parents=" + Arrays.toString(parents) +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Commit {
        private Developer author;
        private Developer committer;
        private String message;

        public Developer getAuthor() {
            return author;
        }

        public Developer getCommitter() {
            return committer;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Commit{" +
                    "author=" + author +
                    ", committer=" + committer +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParentCommit{
        private String sha;

        public String getSha() {
            return sha;
        }

        @Override
        public String toString() {
            return "ParentCommit{" +
                    "sha='" + sha + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Developer {
        private String name;
        private String email;
        private String date;

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getDate() {
            return date;
        }

        @Override
        public String toString() {
            return "Developer{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", date=" + date +
                    '}';
        }
    }
}
