package by.tms.lesson17.homework;

import by.tms.lesson17.homework.exceptions.ExceededTimeLimitException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PostService {

    private final int limitPostsFromOneUserPerDuration;
    private final Duration postDelayDuration;
    private final List<Post> postHistory;

    public PostService(int limitPostsFromOneUserPerDuration, Duration postDelayDuration) {
        this.postDelayDuration = postDelayDuration;
        this.limitPostsFromOneUserPerDuration = limitPostsFromOneUserPerDuration;
        postHistory = new ArrayList<>();
    }

    public void addNewPost(User user, String message) throws ExceededTimeLimitException {

        Instant messageTime = getCurrentTime();

        int messageCounter = 0;
        Instant deltaTime = messageTime.minus(postDelayDuration);

        for (int i = postHistory.size() - 1; i > 0; i--) {

            if (postHistory.get(i).getMessageTime().isBefore(deltaTime)) {
                break;
            }

            if (postHistory.get(i).getMessageTime().isAfter(deltaTime)
                    && (postHistory.get(i).getAuthor().getNickName().equals(user.getNickName()))) {

                messageCounter++;

                if (messageCounter == limitPostsFromOneUserPerDuration - 1) {
                    throw new ExceededTimeLimitException(postHistory.get(i).getMessageTime().plus(postDelayDuration));
                }
            }

        }

        saveNewPost(new Post(user, message, messageTime));
    }

    public List<Post> getAllPosts() {
        return java.util.Collections.unmodifiableList(postHistory);
    }

    private Instant getCurrentTime() {
        return Instant.now();
    }

    private void saveNewPost(Post newPost) {
        postHistory.add(newPost);
    }

}
