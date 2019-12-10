package com.trungnh103.shapeChallenge.controller;

import com.google.gson.Gson;
import com.trungnh103.shapeChallenge.model.User;
import com.trungnh103.shapeChallenge.service.CustomUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService userService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void test_showLoginForm() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void test_showSignUpForm() throws Exception {
        mvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    public void test_createNewUser() throws Exception {
        User ronaldo = new User("ronaldo", null);
        Mockito.when(userService.findUserByUsername(any())).thenReturn(null);
        Gson gson = new Gson();
        String content = gson.toJson(ronaldo);
        mvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("successMessage", "User has been registered successfully"))
                .andExpect(view().name("login"));
        verify(userService, times(1)).findUserByUsername(any());
        verify(userService, times(1)).saveUser(any(), anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_home() throws Exception {
        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }
}
