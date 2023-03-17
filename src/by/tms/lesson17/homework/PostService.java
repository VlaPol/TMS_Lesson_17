package by.tms.lesson17.homework;

import by.tms.lesson17.homework.exceptions.ExceededTimeLimitException;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class PostService {

    private final int limitPostsFromOneUserPerDuration;
    private final Duration postDelayDuration;
    private Post[] postHistory;

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

                if (messageCounter == limitPostsFromOneUserPerDuration - 1){
                    throw new ExceededTimeLimitException(postHistory[i].getMessageTime().plus(postDelayDuration));
                }
            }

        }

        saveNewPost(new Post(user, message, messageTime));
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
