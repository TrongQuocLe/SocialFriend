package com.socialfriend.backend;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<String> viewFriendRecs(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            List<User> friendRecs = userRepository.findRecsByUsername(username);
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
        List<User> popularUsers = userRepository.findPopularUsers();
        Set<String> popularUsernames = new HashSet<>();
        for (User popularUser : popularUsers) {
            popularUsernames.add(popularUser.getUsername());
        }
        return popularUsernames;
    }

    public Set<String> viewMutual(String username, String otherUsername) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            List<User> mutualFollowings = userRepository.findCommonUsers(username, otherUsername);
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
