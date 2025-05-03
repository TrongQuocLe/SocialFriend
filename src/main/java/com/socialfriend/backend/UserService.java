package com.socialfriend.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String name, String username, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password));
    }

    public Optional<User> viewProfile(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> editProfile(String username, String newName, String newEmail) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        userOpt.ifPresent(user -> {
            user.setName(newName);
            user.setEmail(newEmail);
            userRepository.save(user);
        });
        return userOpt;
    }

    public boolean follow(String followerUsername, String followeeUsername) {
        Optional<User> followerOpt = userRepository.findByUsername(followerUsername);
        Optional<User> followeeOpt = userRepository.findByUsername(followeeUsername);

        if (followerOpt.isPresent() && followeeOpt.isPresent()) {
            User follower = followerOpt.get();
            User followee = followeeOpt.get();
            follower.getFollows().add(followee);
            userRepository.save(follower);
            return true;
        }
        return false;
    }

    public boolean unfollow(String followerUsername, String followeeUsername) {
        Optional<User> followerOpt = userRepository.findByUsername(followerUsername);
        Optional<User> followeeOpt = userRepository.findByUsername(followeeUsername);

        if (followerOpt.isPresent() && followeeOpt.isPresent()) {
            User follower = followerOpt.get();
            User followee = followeeOpt.get();
            follower.getFollows().remove(followee);
            userRepository.save(follower);
            return true;
        }
        return false;
    }

        public Set<String> viewFollowing(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            Set<String> followingUsernames = new HashSet<>();
            for (User followedUser : userOpt.get().getFollows()) {
                followingUsernames.add(followedUser.getUsername());  // Add the username to the set
            }
            return followingUsernames;
        }
        else {
            return new HashSet<>();
        }
        
    }
    public Set<String> viewFollowers(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            List<User> followers = userRepository.findFollowersByUsername(username);
            Set<String> followerUsernames = new HashSet<>();
            for (User follower : followers) {
                followerUsernames.add(follower.getUsername());
            }
            return followerUsernames;
        }
        else {
            return new HashSet<>();
        }
        
    }
}
