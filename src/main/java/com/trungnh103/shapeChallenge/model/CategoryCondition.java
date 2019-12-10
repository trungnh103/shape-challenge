package com.trungnh103.shapeChallenge.model;

import lombok.Data;

@Data
public class CategoryCondition {
    private String category;
    private String condition;

    public String toString() {
        return category == null ? "" : category + " when " + condition;
    }

    public CategoryCondition() {
    }

    public CategoryCondition(String category, String condition) {
        this.category = category;
        this.condition = condition;
    }
}
