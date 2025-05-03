package com.socialfriend.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.Set;
import java.util.Scanner;

public class CliApp {
    private static UserService userService;
    private static Scanner scanner = new Scanner(System.in);
    private static String currentUsername = null;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MainApplication.class);
        userService = context.getBean(UserService.class);

        while (true) {
            if (currentUsername == null) {
                System.out.println("\n== Welcome to SocialFriend ==");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose: ");
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) register();
                else if (choice == 2) login();
                else break;
            } else {
                showMenu();
            }
        }
    }

    private static void register() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userService.registerUser(name, username, email, password);
        System.out.println("✅ Registered as " + user.getUsername());
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> user = userService.login(username, password);
        if (user.isPresent()) {
            currentUsername = username;
            System.out.println("✅ Login successful!");
        } else {
            System.out.println("❌ Invalid credentials.");
        }
    }

    private static void showMenu() {
        System.out.println("\n== Hello " + currentUsername + " ==");
        System.out.println("1. View Profile");
        System.out.println("2. Edit Profile");
        System.out.println("3. Follow User");
        System.out.println("4. Unfollow User");
        System.out.println("5. View Following");
        System.out.println("6. View Followers");
        System.out.println("7. Friend Recommendations");
        System.out.println("8. View Popular Users");
        System.out.println("9. Logout");
        System.out.print("Choose: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> viewProfile();
            case 2 -> editProfile();
            case 3 -> followUser();
            case 4 -> unfollowUser();
            case 5 -> viewFollowing();
            case 6 -> viewFollowers();
            case 7 -> viewFriendRecs();
            case 8 -> viewPopular();
            case 9 -> {
                currentUsername = null;
                System.out.println("👋 Logged out.");
            }
            default -> System.out.println("❌ Invalid choice.");
        }
    }

    private static void viewProfile() {
        Optional<User> user = userService.viewProfile(currentUsername);
        user.ifPresentOrElse(u -> {
            System.out.println("Name: " + u.getName());
            System.out.println("Username: " + u.getUsername());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Follows: " + u.getFollows().size() + " users");
        }, () -> System.out.println("❌ Profile not found."));
    }

    private static void editProfile() {
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();

        userService.editProfile(currentUsername, name, email);
        System.out.println("✅ Profile updated.");
    }

    private static void followUser() {
        System.out.print("Enter username to follow: ");
        String followee = scanner.nextLine();
        if (userService.follow(currentUsername, followee)) {
            System.out.println("✅ You are now following " + followee);
        } else {
            System.out.println("❌ Unable to follow user.");
        }
    }

    private static void unfollowUser() {
        System.out.print("Enter username to unfollow: ");
        String followee = scanner.nextLine();
        if (userService.unfollow(currentUsername, followee)) {
            System.out.println("✅ You unfollowed " + followee);
        } else {
            System.out.println("❌ Unable to unfollow user.");
        }
    }

    private static void viewFollowing() {
        Set<String> following = userService.viewFollowing(currentUsername);
        System.out.print("These are users you follow:\n" + following);
    }

    private static void viewFollowers() {
        Set<String> followers = userService.viewFollowers(currentUsername);
        System.out.print("These are users who follow you:\n" + followers);
    }

    private static void viewFriendRecs() {
    Set<String> friendRecs = userService.viewFriendRecs(currentUsername);
    System.out.print("These are your friend recommendations:\n" + friendRecs);
    }

    private static void viewPopular() {
    Set<String> popularUsers = userService.viewPopular();
    System.out.print("These are the top ten most popular users:\n" + popularUsers);
    }
}
