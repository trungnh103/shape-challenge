package com.trungnh103.shapeChallenge.repository;

import com.trungnh103.shapeChallenge.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    
    Role findByRole(String role);
}
