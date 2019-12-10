package com.trungnh103.shapeChallenge.service;

import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.CategoryCondition;
import com.trungnh103.shapeChallenge.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class CategoryServiceTest {

    @TestConfiguration
    static class CategoryServiceTestContextConfiguration {
        @Bean
        public CategoryService categoryService() {
            return new CategoryServiceImpl();
        }
    }

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @Before
    public void setup() {
        Category square = new Category();
        square.setName("SQUARE");
        square.setId("1");
        square.setRequirements(Arrays.asList("size"));
        square.setAreaFormula("size*size");

        Category rectangle = new Category();
        rectangle.setName("RECTANGLE");
        rectangle.setId("2");
        rectangle.setRequirements(Arrays.asList("width", "height"));
        rectangle.setAreaFormula("width*height");
        CategoryCondition categoryCondition = new CategoryCondition("SQUARE", "width==height");
        rectangle.setConditionsToBecomeOtherCategories(Arrays.asList(categoryCondition));

        List<Category> categories = Arrays.asList(square, rectangle);

        Mockito.when(categoryRepository.findAll()).thenReturn(categories);
    }

    @Test
    public void test_findAll() {
        Category square = new Category();
        square.setName("SQUARE");

        Category rectangle = new Category();
        rectangle.setName("RECTANGLE");

        List<Category> categories = Arrays.asList(square, rectangle);

        List<Category> found = categoryService.findAll();
        verifyFindAllIsCalledOnce();
        assertThat(found.size()).isEqualTo(2);
        assertThat(found.stream().map(category -> category.getName()).collect(Collectors.toList())).contains("SQUARE", "RECTANGLE");
    }

    private void verifyFindAllIsCalledOnce() {
        Mockito.verify(categoryRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(categoryRepository);
    }

}
