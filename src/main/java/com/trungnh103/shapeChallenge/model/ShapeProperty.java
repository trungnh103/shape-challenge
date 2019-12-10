package com.trungnh103.shapeChallenge.model;

import lombok.Data;

@Data
public class ShapeProperty {
    private String name;
    private String value;

    public ShapeProperty(String name, String value) {
        this.value = value;
        this.name = name;
    }

    public String toString() {
        return name + ": " + value;
    }
}
