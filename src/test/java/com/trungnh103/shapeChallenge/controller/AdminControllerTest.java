package com.trungnh103.shapeChallenge.controller;

import com.trungnh103.shapeChallenge.model.Role;
import com.trungnh103.shapeChallenge.model.User;
import com.trungnh103.shapeChallenge.service.CustomUserDetailsService;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        User messi = new User("messi", "p@ssw0rd");
        messi.setRoles(Sets.newHashSet(Arrays.asList(new Role("ADMIN"))));
        User pep = new User("pep", null);
        pep.setRoles(Sets.newHashSet(Arrays.asList(new Role("ADMIN"))));
        User ronaldo = new User("ronaldo", null);
        ronaldo.setRoles(Sets.newHashSet(Arrays.asList(new Role("KID"))));
        List<User> users = Arrays.asList(messi, ronaldo, pep);
        List<User> admins = Arrays.asList(messi, pep);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("trungnh103", null);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userDetailsService.findAllActiveAdmins()).thenReturn(admins);

    }

    @Test
    public void test_showAdminList() throws Exception {
        mvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(view().name("admins"))
                .andExpect(model().attribute("admins", hasSize(2)))
                .andExpect(model().attribute("admins", hasItem(
                        allOf(
                                hasProperty("username", is("pep"))
                        )
                )))
                .andExpect(model().attribute("admins", hasItem(
                        allOf(
                                hasProperty("username", is("messi"))
                        )
                )));
    }

    @Test
    public void test_showCreateAdminForm() throws Exception {
        mvc.perform(get("/admins/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-admin"));
    }

}
