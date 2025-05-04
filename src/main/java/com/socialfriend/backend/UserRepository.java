package com.socialfriend.backend;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends Neo4jRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("MATCH (current:User{username: $username})-[:FOLLOWS]->(friend:User)-[:FOLLOWS]->(recommended:User) WHERE NOT (current)-[:FOLLOWS]->(recommended) AND current <> recommended RETURN DISTINCT recommended")
    List<User> findRecsByUsername(@Param("username") String username);

    @Query("MATCH (follower:User)-[:FOLLOWS]->(user:User) WITH user, COUNT(follower) AS followerCount RETURN user ORDER BY followerCount DESC LIMIT 10")
    List<User> findPopularUsers();

    @Query("MATCH (u1: User{username: $username})-[:FOLLOWS]->(mutual:User)<-[:FOLLOWS]-(u2:User{username: $otherUsername}) RETURN mutual")
    List<User> findCommonUsers(@Param("username") String username, @Param("otherUsername") String otherUsername);

    @Query("CREATE (u:User {id: $id, username: $username, name: $name, email: $email, password: $password}) RETURN u")
    User register(@Param("id") Long id, @Param("username") String username, @Param("name") String name, @Param("email") String email, @Param("password") String password);

    @Query("""
    MATCH (u:User {username: $username, password: $password})
    RETURN u
    """)
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query("""
    MATCH (u:User)
    WHERE toLower(u.username) CONTAINS $query OR toLower(u.name) CONTAINS $query
    RETURN u
    """)
    List<User> findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(@Param("query") String $query);

    @Query("MATCH (u:User {username: $username}) RETURN u")
    Optional<User> getProfile(@Param("username") String username);

    @Query("MATCH (u:User {username: $username}) SET u.name = $name, u.email = $email, u.bio = $bio RETURN u")
    Optional<User> updateProfile(@Param("username") String username, @Param("name") String name, @Param("email") String email, @Param("bio") String bio);

    @Query("""
    MATCH (fromNode:User {username: $fromUsername})
    OPTIONAL MATCH (toNode:User {username: $toUsername})
    WHERE fromNode <> toNode
    MERGE (fromNode)-[:FOLLOWS]->(toNode)
    """)
    Optional<User> follow(@Param("fromUsername") String fromUsername, @Param("toUsername") String toUsername);

    @Query("""
    MATCH (u:User)-[:FOLLOWS]->(f:User {username: $username})
    RETURN COUNT(f)
    """)
    Long getNumberOfFollower(@Param("username") String username);

    @Query("""
    MATCH (fromNode:User {username: $fromUsername})-[r:FOLLOWS]->(toNode:User {username: $toUsername})
    DELETE r
    """)
    Optional<User> unfollow(@Param("fromUsername") String fromUsername, @Param("toUsername") String toUsername);

    @Query("""
    MATCH (a:User)-[:FOLLOWS]->(x:User)<-[:FOLLOWS]-(b:User)
    WHERE id(a) = $userA AND id(b) = $userB
    RETURN DISTINCT x
    """)
    List<User> getMutualConnections(@Param("userA") Long userA, @Param("userB") Long userB);

    @Query("""
    MATCH (me:User)-[:FOLLOWS]->(f1:User)-[:FOLLOWS]->(rec:User)
    WHERE id(me) = $userId AND NOT (me)-[:FOLLOWS]->(rec) AND id(me) <> id(rec)
    RETURN DISTINCT rec LIMIT 10
    """)
    List<User> getRecommendations(@Param("userId") Long userId);

    @Query("""
    MATCH (u:User)<-[:FOLLOWS]-(f)
    RETURN u.username, COUNT(f) AS followers
    ORDER BY followers DESC
    LIMIT 10
    """)
    List<User> getPopularUsers();

    @Query("""
    MATCH (u:User) RETURN count(u) AS total_users;
    """)
    Long getTotalUsers();

    @Query("""
    MATCH (follower:User {username: $username})-[:FOLLOWS]->(followee:User) RETURN followee
    """)
    List<User> findFollowingByUsername(@Param("username") String username);

    @Query("""
    MATCH (follower:User)-[:FOLLOWS]->(followee:User {username: $username}) RETURN follower
    """)
    List<User> findFollowersByUsername(@Param("username") String username);
}
