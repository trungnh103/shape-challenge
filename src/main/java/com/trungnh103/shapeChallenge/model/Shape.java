package com.trungnh103.shapeChallenge.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document(collection = "shape")
public class Shape {
    @Id
    private String id;

    private String name;

    @DBRef
    private Category category;

    private Set<ShapeProperty> shapeProperties;

    private String username;

    public Shape(Category category, String name) {
        this.category = category;
        this.name = name;
    }
}
