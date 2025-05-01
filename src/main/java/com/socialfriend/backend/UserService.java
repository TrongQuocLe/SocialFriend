package com.socialfriend.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
