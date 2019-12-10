package com.trungnh103.shapeChallenge.service;

import com.trungnh103.shapeChallenge.common.MathExpressionEvaluator;
import com.trungnh103.shapeChallenge.dto.ShapeDto;
import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.CategoryCondition;
import com.trungnh103.shapeChallenge.model.ShapeProperty;
import com.trungnh103.shapeChallenge.model.Shape;
import com.trungnh103.shapeChallenge.repository.CategoryRepository;
import com.trungnh103.shapeChallenge.repository.ShapeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShapeServiceImpl implements ShapeService {
    @Autowired
    ShapeRepository shapeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Shape createShape(Shape shape) {
        String categoryId = shape.getCategory().getId();
        Category category = categoryRepository.findById(categoryId).orElse(shape.getCategory());
        shape.setCategory(category);
        return shapeRepository.save(shape);
    }

    @Override
    public Shape updateShape(Shape shape) {
        return shapeRepository.save(shape);
    }

    @Override
    public List<Shape> getSavedShapes(String username) {
        return shapeRepository.findByUsername(username);
    }

    @Override
    public Optional<Shape> findById(String shapeId) {
        return shapeRepository.findById(shapeId);
    }

    @Override
    public String calculateArea(Shape shape) {
        Category category = shape.getCategory();
        String areaFormula = category.getAreaFormula();
        Iterator shapePropertyIterator = shape.getShapeProperties().iterator();
        while (shapePropertyIterator.hasNext()) {
            ShapeProperty shapeProperty = (ShapeProperty) shapePropertyIterator.next();
            areaFormula = areaFormula.replace(shapeProperty.getName(), shapeProperty.getValue());
        }
        return MathExpressionEvaluator.evalFormula(areaFormula);
    }

    @Override
    public Set<String> getPossibleCategories(Shape shape) {
        Set<String> possibleCategories = new HashSet();
        possibleCategories.add(shape.getCategory().getName());
        Category category = shape.getCategory();

        Iterator conditionIterator = category.getConditions().iterator();
        while (conditionIterator.hasNext()) {
            CategoryCondition condition = (CategoryCondition) conditionIterator.next();
            String conditionStr = condition.getCondition();
            Iterator shapePropertyIterator = shape.getShapeProperties().iterator();
            while (shapePropertyIterator.hasNext()) {
                ShapeProperty shapeProperty = (ShapeProperty) shapePropertyIterator.next();
                conditionStr = conditionStr.replace(shapeProperty.getName(), shapeProperty.getValue());
                if (MathExpressionEvaluator.evalCondition(conditionStr)) {
                    possibleCategories.add(condition.getCategory());
                }
            }
        }

        return possibleCategories;
    }

    @Override
    public List<Shape> findAll() {
        return shapeRepository.findAll();
    }

    @Override
    public Shape convertFromDto(ShapeDto shapeDto) {
        Category category = categoryRepository.findByName(shapeDto.getCategory());
        Shape shape = new Shape(category, shapeDto.getName());
        if (shapeDto.getId() != null) {
            shape.setId(shapeDto.getId());
        }

        String[] properties = shapeDto.getProperties().split(",");
        int i = 0;
        Set<ShapeProperty> shapeProperties = new HashSet<>();
        for (String requirement : category.getRequirements()) {
            ShapeProperty shapeProperty = new ShapeProperty(requirement, properties[i++]);
            shapeProperties.add(shapeProperty);
        }
        shape.setShapeProperties(shapeProperties);
        shape.setUsername(shapeDto.getKid());
        return shape;
    }

    @Override
    public ShapeDto convertToDto(Shape shape) {
        ShapeDto shapeDto = new ShapeDto();
        shapeDto.setId(shape.getId());
        shapeDto.setCategory(shape.getCategory() == null ? "" : shape.getCategory().getName());
        shapeDto.setKid(shape.getUsername());
        shapeDto.setName(shape.getName());
        StringBuilder properties = new StringBuilder();
        for (ShapeProperty shapeProperty : shape.getShapeProperties()) {
            properties.append(shapeProperty.getValue());
            properties.append(",");
        }
        int length = properties.length();
        properties.delete(length - 1, length);
        shapeDto.setProperties(properties.toString());
        return shapeDto;
    }

    @Override
    public void delete(String id) {
        shapeRepository.deleteById(id);
    }
}
