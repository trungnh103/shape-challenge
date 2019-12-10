package com.trungnh103.shapeChallenge.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document(collection = "user")
public class User {
    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean active;
    @DBRef
    private Set<Role> roles;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
