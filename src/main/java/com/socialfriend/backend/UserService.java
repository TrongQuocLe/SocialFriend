package com.socialfriend.backend;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    
    public User register(User user) {
        // return userRepo.save(user);
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

    public List<User> searchUsers(String query) {
        return userRepo.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(query, query);
    }

    public Optional<User> updateProfile(String username, String name, String email,String bio) {
        return userRepo.updateProfile(username, name, email, bio);
    }

    public List<User> getFollowing(Long userId) {
        return userRepo.getFollowing(userId);
    }
    public List<User> getFollowers(Long userId) {
        return userRepo.getFollowers(userId);
    }
    public List<User> getMutualConnections(Long userA, Long userB) {
        return userRepo.getMutualConnections(userA, userB);
    }
    public List<User> getRecommendations(Long userId) {
        return userRepo.getRecommendations(userId);
    }
    public List<User> getPopularUsers() {
        return userRepo.getPopularUsers();
    }
    public Long getTotalUsers() {
        return userRepo.getTotalUsers();
    }
}


// @Service
// public class UserService {

//     @Autowired
//     private UserRepository userRepository;

//     public User registerUser(String name, String username, String email, String password) {
//         User user = new User();
//         user.setName(name);
//         user.setUsername(username);
//         user.setEmail(email);
//         user.setPassword(password);
//         return userRepository.save(user);
//     }

//     public Optional<User> login(String username, String password) {
//         return userRepository.findByUsername(username)
//                 .filter(u -> u.getPassword().equals(password));
//     }

//     public Optional<User> viewProfile(String username) {
//         return userRepository.findByUsername(username);
//     }

//     public Optional<User> editProfile(String username, String newName, String newEmail) {
//         Optional<User> userOpt = userRepository.findByUsername(username);
//         userOpt.ifPresent(user -> {
//             user.setName(newName);
//             user.setEmail(newEmail);
//             userRepository.save(user);
//         });
//         return userOpt;
//     }

//     public boolean follow(String followerUsername, String followeeUsername) {
//         Optional<User> followerOpt = userRepository.findByUsername(followerUsername);
//         Optional<User> followeeOpt = userRepository.findByUsername(followeeUsername);

//         if (followerOpt.isPresent() && followeeOpt.isPresent()) {
//             User follower = followerOpt.get();
//             User followee = followeeOpt.get();
//             follower.getFollows().add(followee);
//             userRepository.save(follower);
//             return true;
//         }
//         return false;
//     }

//     public boolean unfollow(String followerUsername, String followeeUsername) {
//         Optional<User> followerOpt = userRepository.findByUsername(followerUsername);
//         Optional<User> followeeOpt = userRepository.findByUsername(followeeUsername);

//         if (followerOpt.isPresent() && followeeOpt.isPresent()) {
//             User follower = followerOpt.get();
//             User followee = followeeOpt.get();
//             follower.getFollows().remove(followee);
//             userRepository.save(follower);
//             return true;
//         }
//         return false;
//     }
// }
