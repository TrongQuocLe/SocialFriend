package com.socialfriend.backend;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query("MATCH (follower:User)-[:FOLLOWS]->(followee:User) WHERE followee.username = $username RETURN follower")
    List<User> findFollowersByUsername(@Param("username") String username);
    @Query("MATCH (current:User{username: $username})-[:FOLLOWS]->(friend:User)-[:FOLLOWS]->(recommended:User) WHERE NOT (current)-[:FOLLOWS]->(recommended) AND current <> recommended RETURN DISTINCT recommended")
    List<User> findRecsByUsername(@Param("username") String username);
    @Query("MATCH (follower:User)-[:FOLLOWS]->(user:User) WITH user, COUNT(follower) AS followerCount RETURN user ORDER BY followerCount DESC LIMIT 10")
    List<User> findPopularUsers();
    @Query("MATCH (u1: User{username: $username})-[:FOLLOWS]->(mutual:User)<-[:FOLLOWS]-(u2:User{username: $otherUsername}) RETURN mutual")
    List<User> findCommonUsers(@Param("username") String username, @Param("otherUsername") String otherUsername);
}
