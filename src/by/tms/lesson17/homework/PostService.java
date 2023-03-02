package by.tms.lesson17.homework;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class PostService {

    private final int limitPosts;
    private final Duration duration;
    private static Post[] postHistory;

    public PostService(int limitPosts, Duration duration) {
        this.duration = duration;
        this.limitPosts = limitPosts;
        postHistory = new Post[0];
    }

    public boolean addNewPost(Post newPost) {

        int messageCounter = 0;

        for (Post itm : postHistory) {
            if (itm.getMessageTime().isBefore(Instant.now().minus(duration))
                    && (itm.getAuthor().equals(newPost.getAuthor()))) {
                messageCounter = 0;
            }
        }

        if (postHistory.length == 0) {
            saveNewPost(newPost);
            messageCounter++;
        } else {

            for (Post itm : postHistory) {

                if (itm.getMessageTime().isAfter(Instant.now().minus(duration))
                        && (itm.getAuthor().equals(newPost.getAuthor()))) {
                    messageCounter++;
                }
            }
            if (messageCounter < limitPosts) {
                saveNewPost(newPost);
                messageCounter++;
            } else {
                return false;
            }
        }
        return true;
    }

    private void saveNewPost(Post newPost) {
        postHistory = Arrays.copyOf(postHistory, postHistory.length + 1);
        postHistory[postHistory.length - 1] = newPost;
    }

    public Post[] getAllPosts() {
        return postHistory;
    }

}
