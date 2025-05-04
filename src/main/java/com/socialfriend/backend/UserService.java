package com.socialfriend.backend;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    
    public User register(User user) {
        return userRepo.register(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getPassword());
    }

    public Optional<User> login(String username, String password) {
        return userRepo.findByUsernameAndPassword(username, password);
    }

    public Optional<User> getProfile(String username) {
        return userRepo.getProfile(username);
    }

    public Optional<User> follow(String fromUsername, String toUsername) {
        return userRepo.follow(fromUsername, toUsername);
    }

    public Long getNumberOfFollower(String username) {
        return userRepo.getNumberOfFollower(username);
    }

    public Optional<User> unfollow(String fromUsername, String toUsername) {
        return userRepo.unfollow(fromUsername, toUsername);
    }

    public Optional<User> updateProfile(String username, String name, String email,String bio) {
        return userRepo.updateProfile(username, name, email, bio);
    }

    public Long getTotalUsers() {
        return userRepo.getTotalUsers();
    }

    public Set<String> viewFollowing (String username) {
        List<User> following = userRepo.findFollowingByUsername(username);
        Set<String> followingUsernames = new HashSet<>();
        for (User follow : following) {
            followingUsernames.add(follow.getUsername());
        }
        return followingUsernames;
    }
    
    public Set<String> viewFollowers (String username) {
        List<User> followers = userRepo.findFollowersByUsername(username);
        Set<String> followersUsernames = new HashSet<>();
        for (User follower : followers) {
            followersUsernames.add(follower.getUsername());
        }
        return followersUsernames;
    }

    public Set<String> viewFriendRecs(String username) {
        Optional<User> userOpt = userRepo.findByUsername(username);

        if (userOpt.isPresent()) {
            List<User> friendRecs = userRepo.findRecsByUsername(username);
            Set<String> friendRecsUsernames = new HashSet<>();
            for (User friendRec : friendRecs) {
                friendRecsUsernames.add(friendRec.getUsername());
            }
            return friendRecsUsernames;
        }
        else {
            return new HashSet<>();
        }
    }

    public Set<String> viewPopular() {
        List<User> popularUsers = userRepo.findPopularUsers();
        Set<String> popularUsernames = new HashSet<>();
        for (User popularUser : popularUsers) {
            popularUsernames.add(popularUser.getUsername());
        }
        return popularUsernames;
    }

    public Set<String> viewMutual(String username, String otherUsername) {
        Optional<User> userOpt = userRepo.findByUsername(username);

        if (userOpt.isPresent()) {
            List<User> mutualFollowings = userRepo.findCommonUsers(username, otherUsername);
            Set<String> mutualFollowingsUsernames = new HashSet<>();
            for (User mutualFollowing : mutualFollowings) {
                mutualFollowingsUsernames.add(mutualFollowing.getUsername());  // Add the username to the set
            }
            return mutualFollowingsUsernames;
        }
        else {
            return new HashSet<>();
        }
    }

    public Set<String> searchUsers(String query) {
        List<User> users = userRepo.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(query);
        Set<String> usernames = new HashSet<>();
        for (User user : users) {
            usernames.add(user.getUsername() + " name (" + user.getName() + ")");
        }
        return usernames;
    }

}