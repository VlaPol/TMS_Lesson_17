package by.tms.lesson17.homework;

import by.tms.lesson17.homework.exceptions.ExceededTimeLimitException;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class PostService {

    private final int limitPostsFromOneUserPerDuration;
    private final Duration postDelayDuration;
    private Post[] postHistory;

    private Post[] timeDelay = new Post[0];

    public PostService(int limitPostsFromOneUserPerDuration, Duration postDelayDuration) {
        this.postDelayDuration = postDelayDuration;
        this.limitPostsFromOneUserPerDuration = limitPostsFromOneUserPerDuration;
        postHistory = new Post[0];
    }

    public void addNewPost(User user, String message) throws ExceededTimeLimitException {

        Instant messageTime = getCurrentTime();

        int messageCounter = 0;
        Instant deltaTime = messageTime.minus(postDelayDuration);

        for (int i = postHistory.length - 1; i > 0; i--) {

            if (postHistory[i].getMessageTime().isBefore(deltaTime)) {
                break;
            }

            if (postHistory[i].getMessageTime().isAfter(deltaTime)
                    && (postHistory[i].getAuthor().getNickName().equals(user.getNickName()))) {

                messageCounter++;

                if (messageCounter == limitPostsFromOneUserPerDuration - 1) {
                    Instant approvedTime = getApprovedTime(user, messageTime, deltaTime);
                    if (approvedTime != null) {
                        throw new ExceededTimeLimitException(approvedTime.plus(postDelayDuration));
                    }
                }
            }

        }

        saveNewPost(new Post(user, message, messageTime));
    }

    private Instant getApprovedTime(User user, Instant messageTime, Instant deltaTime) {

        if (timeDelay.length == 0) {
            timeDelay = Arrays.copyOf(timeDelay, timeDelay.length + 1);
            timeDelay[0] = new Post(user, null, messageTime);
            return timeDelay[0].getMessageTime();
        }

        for (int i = timeDelay.length - 1; i >= 0; i--) {
            if (timeDelay[i].getMessageTime().isAfter(deltaTime)
                    && timeDelay[i].getAuthor().getNickName().equals(user.getNickName())) {
                return timeDelay[i].getMessageTime();
            } else {
                timeDelay = Arrays.copyOf(timeDelay, timeDelay.length + 1);
                timeDelay[timeDelay.length - 1] = new Post(user, null, messageTime);
                return timeDelay[timeDelay.length - 1].getMessageTime();
            }

        }
        return null;
    }

    public Post[] getAllPosts() {
        return Arrays.copyOf(postHistory, postHistory.length);
    }

    private Instant getCurrentTime() {
        return Instant.now();
    }

    private void saveNewPost(Post newPost) {
        postHistory = Arrays.copyOf(postHistory, postHistory.length + 1);
        postHistory[postHistory.length - 1] = newPost;
    }


}
