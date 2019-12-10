package com.trungnh103.shapeChallenge.controller;

import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.service.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    CategoryService categoryService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        Category square = new Category();
        square.setName("SQUARE");

        Category rectangle = new Category();
        rectangle.setName("RECTANGLE");

        List<Category> categories = Arrays.asList(square, rectangle);
        Mockito.when(categoryService.findAll()).thenReturn(categories);
    }

    @Test
    public void test_showCreateCategoryForm() throws Exception {
        mvc.perform(get("/categories/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-category"));
    }

    @Test
    public void test_showCategoryList() throws Exception {
        mvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attribute("categories", hasSize(2)))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                                hasProperty("name", is("SQUARE"))
                        )
                )))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                                hasProperty("name", is("RECTANGLE"))
                        )
                )));
        verify(categoryService, times(1)).findAll();
        verifyNoMoreInteractions(categoryService);
    }
}
