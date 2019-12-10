package com.trungnh103.shapeChallenge.repository;

import com.trungnh103.shapeChallenge.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    List<User> findByActive(boolean active);
}
