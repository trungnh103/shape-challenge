package com.trungnh103.shapeChallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.CategoryCondition;
import com.trungnh103.shapeChallenge.model.Shape;
import com.trungnh103.shapeChallenge.model.ShapeProperty;
import com.trungnh103.shapeChallenge.service.CustomUserDetailsService;
import com.trungnh103.shapeChallenge.service.ShapeService;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiControllerTest {
    private static final ObjectMapper om = new ObjectMapper();
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    ShapeService shapeService;

    @MockBean
    private CustomUserDetailsService userService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
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
        rectangle.setConditions(Arrays.asList(categoryCondition));

        Shape tokyoSquare = new Shape(square, "TOKYO SQUARE");
        tokyoSquare.setId("1");
        tokyoSquare.setUsername("SUPER KID");
        Shape tokyoRectangle = new Shape(rectangle, "TOKYO RECTANGLE");
        tokyoRectangle.setId("2");
        tokyoRectangle.setUsername("WONDER KID");

        List<Category> categories = Arrays.asList(square, rectangle);
        List<Shape> shapes = Arrays.asList(tokyoSquare, tokyoRectangle);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("trungnh103", null);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(shapeService.getAllCategories()).thenReturn(categories);
        Mockito.when(shapeService.getSavedShapes(authentication.getName())).thenReturn(shapes);
    }

    @Test
    public void test_getSavedShapes() throws Exception {
        mvc
                .perform(get("/api/shapes/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].username", is("SUPER KID")))
                .andExpect(jsonPath("$[0].name", is("TOKYO SQUARE")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].username", is("WONDER KID")))
                .andExpect(jsonPath("$[1].name", is("TOKYO RECTANGLE")));
        verify(shapeService, times(1)).getSavedShapes(any());
        verifyNoMoreInteractions(shapeService);
    }

    @Test
    public void test_updateShape() throws Exception {
        Category rectangle = new Category();
        rectangle.setName("RECTANGLE");
        Shape tokyoRectangle = new Shape(rectangle, "TOKYO RECTANGLE");
        ShapeProperty width = new ShapeProperty("width", "4");
        ShapeProperty height = new ShapeProperty("height", "6");
        tokyoRectangle.setShapeProperties(Sets.newHashSet(Arrays.asList(width, height)));

        String shapeId = "1";
        Shape updatedShape = new Shape(null, null);
        Mockito.when(shapeService.findById(shapeId)).thenReturn(Optional.of(updatedShape));
        Mockito.when(shapeService.updateShape(updatedShape)).thenReturn(tokyoRectangle);

        Gson gson = new Gson();
        String content = gson.toJson(tokyoRectangle);

        mvc
                .perform(put("/api/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TOKYO RECTANGLE")))
                .andExpect(jsonPath("$.category.name", is("RECTANGLE")))
                .andExpect(jsonPath("$.shapeProperties", hasSize(2)));


        verify(shapeService, times(1)).findById(shapeId);
        verify(shapeService, times(1)).updateShape(updatedShape);
        verifyNoMoreInteractions(shapeService);
    }

    @Test
    public void test_createShape() throws Exception {
        Shape tokyoRectangle = new Shape(new Category(), "TOKYO RECTANGLE");

        Mockito.when(shapeService.createShape(any())).thenReturn(tokyoRectangle);
        Mockito.when(shapeService.calculateArea(any())).thenReturn("20");
        Mockito.when(shapeService.getPossibleCategories(any())).thenReturn(Sets.newHashSet(Arrays.asList("SQUARE")));

        Gson gson = new Gson();
        String content = gson.toJson(tokyoRectangle);

        mvc
                .perform(post("/api/shapes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Area: 20, possible categories: [SQUARE]"));
        verify(shapeService, times(1)).createShape(any());
        verify(shapeService, times(1)).calculateArea(any());
        verify(shapeService, times(1)).getPossibleCategories(any());
        verifyNoMoreInteractions(shapeService);
    }

    @Test
    public void test_getAllCategories() throws Exception {
        mvc
                .perform(get("/api/shapes/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].areaFormula", is("size*size")))
                .andExpect(jsonPath("$[0].name", is("SQUARE")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].areaFormula", is("width*height")))
                .andExpect(jsonPath("$[1].name", is("RECTANGLE")));
        verify(shapeService, times(1)).getAllCategories();
        verifyNoMoreInteractions(shapeService);
    }

}
