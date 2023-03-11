import by.tms.lesson17.homework.PostService;
import by.tms.lesson17.homework.User;
import by.tms.lesson17.homework.exceptions.ExceededTimeLimitException;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Scanner;

public class Homework17Test {

    private static PostService ps;

    public static void main(String[] args) {

        System.out.println("Before start chatting set settings: number of post and delay duration in seconds");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Number of posts: ");
        int numberOfPosts = Integer.parseInt(scanner.nextLine());
        System.out.print("Delay duration in seconds: ");
        Duration duration = Duration.ofSeconds(Integer.parseInt(scanner.nextLine()));

        do {
            System.out.println("******************************");
            System.out.println("***  WELCOME TO OUR CHAT *****");
            System.out.println("******************************");
            System.out.println("1. Chatting ");
            System.out.println("2. History ");
            System.out.println("0. Exit");
            System.out.println("******************************");

            System.out.print("Your choice: ");

            int key = Integer.parseInt(scanner.nextLine());

            switch (key) {
                case 1 -> {
                    ps = new PostService(numberOfPosts, duration);
                    int counter = 0;
                    do {
                        System.out.print("Enter nickname: ");
                        User user = new User(scanner.nextLine());
                        System.out.print("Enter message: ");
                        String message = scanner.nextLine();
                        try {
                            ps.addNewPost(user, message);
                            System.out.println("Post added");
                        } catch (ExceededTimeLimitException e) {
                            System.out.println("To many requests! Try attempt after: "
                                    + Duration.between(Instant.now(), e.getLeftTimeToPost()).getSeconds() + " seconds");
                        }
                        if (counter++ == 20) break;
                    } while (true);
                }
                case 2 -> {
                    System.out.println("Chat history:");
                    System.out.println(Arrays.toString(ps.getAllPosts()));
                }
                case 0 -> {
                    System.out.println("See you!!!");
                    return;
                }
            }
        } while (true);

    }
}