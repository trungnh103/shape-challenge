package com.trungnh103.shapeChallenge.repository;

import com.trungnh103.shapeChallenge.model.Shape;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ShapeRepository extends MongoRepository<Shape, String> {
    List<Shape> findByUsername(String username);
}
