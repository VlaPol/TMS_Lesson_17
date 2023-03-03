package by.tms.lesson17.homework;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// информация об одном сообщении в чате: автор сообщения,
// текстовое содержание сообщения, время отправки
public class Post {
    private final User author;
    private final String message;
    private final Instant createdMessageTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm")
            .withZone(ZoneId.of("Europe/Minsk"));

    public Post(User author, String message, Instant createdMessageTime) {
        this.author = author;
        this.message = message;
        this.createdMessageTime = createdMessageTime;
    }

    public User getAuthor() {
        return author;
    }

    public Instant getMessageTime() {
        return createdMessageTime;
    }

    @Override
    public String toString() {
        return "Post{" +
                "author= " + author +
                ", message= '" + message + '\'' +
                ", messageTime= " + formatter.format(createdMessageTime) +
                '}';
    }
}
