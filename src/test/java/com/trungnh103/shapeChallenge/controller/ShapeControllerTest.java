package com.trungnh103.shapeChallenge.controller;

import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.Shape;
import com.trungnh103.shapeChallenge.model.User;
import com.trungnh103.shapeChallenge.service.CategoryService;
import com.trungnh103.shapeChallenge.service.CustomUserDetailsService;
import com.trungnh103.shapeChallenge.service.ShapeService;
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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShapeControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    private ShapeService shapeService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CustomUserDetailsService userService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
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

        User ronaldo = new User("ronaldo", null);
        User neymar = new User("neymar", null);

        Mockito.when(shapeService.findAll()).thenReturn(Arrays.asList(tokyoSquare, tokyoRectangle));
        Mockito.when(categoryService.findAll()).thenReturn(Arrays.asList(square, rectangle));
        Mockito.when(userService.findKids()).thenReturn(Arrays.asList(ronaldo, neymar));
    }

    @Test
    public void test_showShapeList() throws Exception {
        mvc.perform(get("/shapes"))
                .andExpect(status().isOk())
                .andExpect(view().name("shapes"))
                .andExpect(model().attribute("shapes", hasSize(2)))
                .andExpect(model().attribute("shapes", hasItem(
                        allOf(
                                hasProperty("name", is("TOKYO SQUARE"))
                        )
                )))
                .andExpect(model().attribute("shapes", hasItem(
                        allOf(
                                hasProperty("name", is("TOKYO RECTANGLE"))
                        )
                )));
        verify(shapeService, times(1)).findAll();
        verifyNoMoreInteractions(shapeService);
    }

    @Test
    public void test_showCreateShapeForm() throws Exception {
        mvc.perform(get("/shapes/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-shape"))
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
                )))
                .andExpect(model().attribute("kids", hasSize(2)))
                .andExpect(model().attribute("kids", hasItem(
                        allOf(
                                hasProperty("username", is("ronaldo"))
                        )
                )))
                .andExpect(model().attribute("kids", hasItem(
                        allOf(
                                hasProperty("username", is("neymar"))
                        )
                )));
        verify(categoryService, times(1)).findAll();
        verify(userService, times(1)).findKids();
        verifyNoMoreInteractions(categoryService);
        verifyNoMoreInteractions(userService);
    }

}
