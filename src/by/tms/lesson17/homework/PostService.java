package by.tms.lesson17.homework;

import by.tms.lesson17.homework.exceptions.ExceededTimeLimitException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.util.Collections.unmodifiableList;

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

        ListIterator<Post> postListIterator = postHistory.listIterator(postHistory.size());
        while (postListIterator.hasPrevious()) {

            Post tmpPost = postListIterator.previous();
            if (tmpPost.getMessageTime().isBefore(deltaTime)) {
                break;
            }

            if (tmpPost.getMessageTime().isAfter(deltaTime)
                    && tmpPost.getAuthor().equals(user)) {
                messageCounter++;

                if (messageCounter == limitPostsFromOneUserPerDuration - 1) {
                    throw new ExceededTimeLimitException(tmpPost.getMessageTime().plus(postDelayDuration));
                }
            }

        }

        saveNewPost(new Post(user, message, messageTime));
    }

    public List<Post> getAllPosts() {
        return unmodifiableList(postHistory);
    }

    private Instant getCurrentTime() {
        return Instant.now();
    }

    private void saveNewPost(Post newPost) {
        postHistory.add(newPost);
    }

}
