package pchila.commitreader.core;


import java.time.ZonedDateTime;

public class Commit {
    private String hash;
    private Author author;
    private Message message;
    private ZonedDateTime authorDate;
    private ZonedDateTime committerDate;

    public Commit(String hash, Author author, Message message, ZonedDateTime authorDate, ZonedDateTime committerDate) {
        this.hash = hash;
        this.author = author;
        this.message = message;
        this.authorDate = authorDate;
        this.committerDate = committerDate;
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

    @Override
    public String toString() {
        return "Commit{" +
                "hash='" + hash + '\'' +
                ", author=" + author +
                ", message=" + message +
                ", authorDate=" + authorDate +
                ", committerDate=" + committerDate +
                '}';
    }

    public static class Author {
        private String name;
        private String email;

        public Author(String name, String email) {
            this.name = name;
            this.email = email;
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
    }

}
