package pchila.commitviewer.core;


import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Commit {
    private String hash;
    private Developer author;
    private Developer committer;
    private String message;
    private String[] references;
    private String[] parentHashes;

    public Commit(String hash, Developer author, Developer committer, String message, String[] references, String[] parentHashes) {
        this.hash = hash;
        this.author = author;
        this.message = message;
        this.committer = committer;
        this.references = references;
        this.parentHashes = parentHashes;
    }

    public String getHash() {
        return hash;
    }

    public Developer getAuthor() {
        return author;
    }

    public String[] getReferences() {
        return references;
    }

    public String[] getParentHashes() {
        return parentHashes;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "hash='" + hash + '\'' +
                ", author=" + author +
                ", committer=" + committer +
                ", message=" + message +
                ", references=" + Arrays.toString(references) +
                ", parentHashes=" + Arrays.toString(parentHashes) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return hash.equals(commit.hash) &&
                author.equals(commit.author) &&
                committer.equals(commit.committer) &&
                message.equals(commit.message) &&
                Arrays.equals(references, commit.references) &&
                Arrays.equals(parentHashes, commit.parentHashes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(hash, author, committer, message);
        result = 31 * result + Arrays.hashCode(references);
        result = 31 * result + Arrays.hashCode(parentHashes);
        return result;
    }

    public static class Developer {
        private String name;
        private String email;
        private ZonedDateTime date;

        public Developer(String name, String email, ZonedDateTime date) {
            this.name = name;
            this.email = email;
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public ZonedDateTime getDate() {
            return date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Developer developer = (Developer) o;
            return name.equals(developer.name) &&
                    email.equals(developer.email) &&
                    date.equals(developer.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, email, date);
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
