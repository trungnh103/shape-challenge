package com.trungnh103.shapeChallenge.service;

import com.trungnh103.shapeChallenge.dto.ShapeDto;
import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.Shape;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ShapeService {
    List<Category> getAllCategories();
    Shape createShape(Shape shape);
    Shape updateShape(Shape shape);
    List<Shape> getSavedShapes(String username);

    Optional<Shape> findById(String shapeId);

    String calculateArea(Shape shape);

    Set getPossibleCategories(Shape shape);

    List<Shape> findAll();

    Shape convertFromDto(ShapeDto shapeDto);

    ShapeDto convertToDto(Shape shape);

    void delete(String id);
}
