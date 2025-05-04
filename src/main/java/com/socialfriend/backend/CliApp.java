package com.socialfriend.backend;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

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
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = new User();
        Long newId = userService.getTotalUsers() + 1;
        user.setId(newId);
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        userService.register(user);
        System.out.println("Registered successfully!");
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        var user = userService.login(username, password);
        if (user.isPresent()) {
            System.out.println("Login successful! Welcome " + user.get().getName());
            currentUsername = username;
        } else {
            System.out.println("Invalid credentials.");
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
        System.out.println("8. View Mutual Friends");
        System.out.println("9. View Friend Recommendations");
        System.out.println("10. Search Users");
        System.out.println("11. View popular users");
        System.out.println("12. Logout");
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            return;
        }

        switch (choice) {
            case 1 -> viewProfile();
            case 2 -> editProfile();
            case 3 -> followUser();
            case 4 -> unfollowUser();
            case 5 -> viewFollowing();
            case 6 -> viewFollowers();
            case 7 -> viewFriendRecs();
            case 8 -> viewMutual();
            case 9 -> viewFriendRecs();
            case 10 -> searchUsers();
            case 11 -> viewPopular();
            case 12 -> {
                currentUsername = null;
                System.out.println("👋 Logged out.");
            }
            default -> System.out.println("❌ Invalid choice.");
        }
    }

    private static boolean isValidChoice(String input, int numberOfChoices) {
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            return false;
        }

        if (choice < 1 || choice > numberOfChoices) {
            System.out.println("❌ Invalid choice. Please enter a number between 1 and " + numberOfChoices + ".");
            return false;
        }
        return true;

    }
    private static void viewProfile() {
        Optional<User> user = userService.getProfile(currentUsername);
        user.ifPresentOrElse(u -> {
            System.out.println("\n== Profile of " + u.getUsername() + " ==");
            System.out.println("Name: " + u.getName());
            System.out.println("Username: " + u.getUsername());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Followers: " + userService.getNumberOfFollower(u.getUsername()));
            System.out.println("Bio: " + (u.getBio() != null ? u.getBio() : "No bio available"));
        }, () -> System.out.println("❌ Profile not found."));
    }

    private static void editProfile() {
        System.out.println("\n== Edit Profile ==");
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        System.out.print("Enter new bio: ");
        String newBio = scanner.nextLine();
        userService.updateProfile(currentUsername, newName, newEmail, newBio);
        System.out.println("✅ Profile updated.");
    }

    private static void followUser() {
        System.out.print("Enter username to follow: ");
        String followeeUsername = scanner.nextLine();
        try {
            userService.follow(currentUsername, followeeUsername);
        } catch (Exception e) {
            System.out.println("❌ Unable to follow user. ");
        } 
        System.out.println("✅ You followed " + followeeUsername);
    }

    private static void unfollowUser() {
        System.out.print("Enter username to unfollow: ");
        String followeeUsername = scanner.nextLine();
        try {
            userService.unfollow(currentUsername, followeeUsername);
            System.out.println("✅ You unfollowed " + followeeUsername);
        } catch (Exception e) {
            System.out.println("❌ Unable to unfollow user. ");
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

    private static void viewMutual() {
        System.out.print("Enter username of who you want to see mutual friends with: ");
        String otherUsername = scanner.nextLine();

        Set<String> mutualFollowers = userService.viewMutual(currentUsername, otherUsername);
        System.out.print("Users followed by both you and " + otherUsername + ":\n" + mutualFollowers);
    }

    private static void searchUsers() {
        System.out.print("Enter username or name to search: ");
        String query = scanner.nextLine();
        query = query.toLowerCase();
        query = query.trim();
        Set<String> users = userService.searchUsers(query);
        System.out.print("Users found:\n" + users);
    }

}

