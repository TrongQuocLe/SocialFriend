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
}
