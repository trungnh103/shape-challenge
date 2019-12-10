package com.trungnh103.shapeChallenge.repository;

import com.trungnh103.shapeChallenge.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findByName(String category);
}
