package com.trungnh103.shapeChallenge.service;

import com.trungnh103.shapeChallenge.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category save(Category category);

    void delete(String id);
}
