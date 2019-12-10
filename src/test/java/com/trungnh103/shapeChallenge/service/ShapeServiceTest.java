package com.trungnh103.shapeChallenge.service;

import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.CategoryCondition;
import com.trungnh103.shapeChallenge.model.Shape;
import com.trungnh103.shapeChallenge.model.ShapeProperty;
import com.trungnh103.shapeChallenge.repository.CategoryRepository;
import com.trungnh103.shapeChallenge.repository.ShapeRepository;
import org.assertj.core.util.Sets;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ShapeServiceTest {
    @TestConfiguration
    static class ShapeServiceTestContextConfiguration {
        @Bean
        public ShapeService userDetailsService() {
            return new ShapeServiceImpl();
        }
    }

    @Autowired
    ShapeService shapeService;
    @MockBean
    ShapeRepository shapeRepository;
    @MockBean
    CategoryRepository categoryRepository;

    @Before
    public void setup() {
        Category square = new Category();
        square.setName("SQUARE");
        Category rectangle = new Category();
        rectangle.setName("RECTANGLE");
        Shape tokyoSquare = new Shape(square, "TOKYO SQUARE");
        tokyoSquare.setId("1");
        tokyoSquare.setUsername("SUPER KID");
        Shape tokyoRectangle = new Shape(rectangle, "TOKYO RECTANGLE");
        tokyoRectangle.setId("2");
        tokyoRectangle.setUsername("WONDER KID");
        Mockito.when(categoryRepository.findAll()).thenReturn(Arrays.asList(square, rectangle));
        Mockito.when(shapeRepository.findByUsername("SUPER KID")).thenReturn(Arrays.asList(tokyoSquare));
        Mockito.when(shapeRepository.findById("1")).thenReturn(Optional.of(tokyoSquare));
        Mockito.when(shapeRepository.findAll()).thenReturn(Arrays.asList(tokyoSquare, tokyoRectangle));
    }

    @Test
    public void test_getPossibleCategories() {
        Category rectangle = new Category();
        rectangle.setName("RECTANGLE");
        CategoryCondition squareCondition = new CategoryCondition("SQUARE", "width==height");
        rectangle.setConditionsToBecomeOtherCategories(Arrays.asList(squareCondition));
        Shape shape = new Shape(rectangle, "TOKYO RECTANGLE");
        ShapeProperty width = new ShapeProperty("width", "4");
        ShapeProperty height = new ShapeProperty("height", "4");
        shape.setShapeProperties(Sets.newHashSet(Arrays.asList(width, height)));
        assertThat(shapeService.getPossibleCategories(shape)).contains("SQUARE");
    }

    @Test
    public void test_findAll() {
        List<Shape> found = shapeService.findAll();
        assertThat(found.size()).isEqualTo(2);
        assertThat(found.stream().map(shape -> shape.getName())).contains("TOKYO SQUARE", "TOKYO RECTANGLE");
    }

    @Test
    public void test_calculateArea() {
        Category rectangle = new Category();
        rectangle.setName("RECTANGLE");
        rectangle.setAreaFormula("width*height");
        Shape shape = new Shape(rectangle, "TOKYO RECTANGLE");
        ShapeProperty width = new ShapeProperty("width", "4");
        ShapeProperty height = new ShapeProperty("height", "5");
        shape.setShapeProperties(Sets.newHashSet(Arrays.asList(width, height)));
        assertThat(shapeService.calculateArea(shape)).isEqualTo("20");
    }

    @Test
    public void test_findById() {
        Optional<Shape> found = shapeService.findById("1");
        verifyFindByIdIsCalledOnce();
        assertThat(found.get().getName()).isEqualTo("TOKYO SQUARE");
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(shapeRepository, VerificationModeFactory.times(1)).findById("1");
        Mockito.reset(shapeRepository);
    }

    @Test
    public void test_getSavedShapes() {
        List<Shape> found = shapeService.getSavedShapes("SUPER KID");
        verifyFindByUsernameIsCalledOnce();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.stream().map(shape -> shape.getName()).collect(Collectors.toList())).contains("TOKYO SQUARE");
    }

    @Test
    public void test_getAllCategories() {
        List<Category> categories = shapeService.getAllCategories();
        List<String> names = categories.stream().map(category -> category.getName()).collect(Collectors.toList());
        verifyFindAllIsCalledOnce();
        assertThat(categories.size()).isEqualTo(2);
        assertThat(names).contains("SQUARE", "RECTANGLE");
    }

    private void verifyFindAllIsCalledOnce() {
        Mockito.verify(categoryRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(categoryRepository);
    }

    private void verifyFindByUsernameIsCalledOnce() {
        Mockito.verify(shapeRepository, VerificationModeFactory.times(1)).findByUsername("SUPER KID");
        Mockito.reset(shapeRepository);
    }

}
