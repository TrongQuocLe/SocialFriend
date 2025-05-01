package com.socialfriend.backend;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String username;
    private String email;
    private String password;

    @Relationship(type = "FOLLOWS")
    private Set<User> follows = new HashSet<>();

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Set<User> getFollows() { return follows; }
    public void setFollows(Set<User> follows) { this.follows = follows; }
}
