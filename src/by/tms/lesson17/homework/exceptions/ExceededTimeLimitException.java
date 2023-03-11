package by.tms.lesson17.homework.exceptions;

import java.time.Instant;

public class ExceededTimeLimitException extends Exception {

    private final Instant leftTimeToPost;

    public ExceededTimeLimitException(Instant instantTime) {
        this.leftTimeToPost = instantTime;
    }

    public Instant getLeftTimeToPost() {
        return leftTimeToPost;
    }
}
