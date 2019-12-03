package pchila.commitviewer.core;


import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Commit {
    private String hash;
    private Author author;
    private Author committer;
    private Message message;
    private ZonedDateTime authorDate;
    private ZonedDateTime committerDate;
    private String[] references;
    private String[] parentHashes;

    public Commit(String hash, Author author, Message message, ZonedDateTime authorDate, Author committer, ZonedDateTime committerDate, String[] references, String[] parentHashes) {
        this.hash = hash;
        this.author = author;
        this.message = message;
        this.authorDate = authorDate;
        this.committer = committer;
        this.committerDate = committerDate;
        this.references = references;
        this.parentHashes = parentHashes;
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
                authorDate.equals(commit.authorDate) &&
                committerDate.equals(commit.committerDate) &&
                Arrays.equals(references, commit.references) &&
                Arrays.equals(parentHashes, commit.parentHashes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(hash, author, committer, message, authorDate, committerDate);
        result = 31 * result + Arrays.hashCode(references);
        result = 31 * result + Arrays.hashCode(parentHashes);
        return result;
    }

    public String getHash() {
        return hash;
    }

    public Author getAuthor() {
        return author;
    }

    public Message getMessage() {
        return message;
    }

    public ZonedDateTime getAuthorDate() {
        return authorDate;
    }

    public ZonedDateTime getCommitterDate() {
        return committerDate;
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
                ", authorDate=" + authorDate +
                ", committerDate=" + committerDate +
                ", references=" + Arrays.toString(references) +
                ", parentHashes=" + Arrays.toString(parentHashes) +
                '}';
    }

    public static class Author {
        private String name;
        private String email;

        public Author(String name, String email) {
            this.name = name;
            this.email = email;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Author author = (Author) o;
            return name.equals(author.name) &&
                    email.equals(author.email);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, email);
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return "Author{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

    public static class Message {
        private String subject;
        private String body;

        public Message(String subject, String body) {
            this.subject = subject;
            this.body = body;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "subject='" + subject + '\'' +
                    ", body='" + body + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Message message = (Message) o;
            return subject.equals(message.subject) &&
                    body.equals(message.body);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subject, body);
        }
    }

}
