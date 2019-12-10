package com.trungnh103.shapeChallenge.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "category")
public class Category {
    @Id
    private String id;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    private String name;

    private List<String> requirements;

    private String areaFormula;

    private List<CategoryCondition> conditionsToBecomeOtherCategories;

    public String toString() {
        return name;
    }

}
