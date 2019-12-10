package com.trungnh103.shapeChallenge.service;

import com.trungnh103.shapeChallenge.model.Role;
import com.trungnh103.shapeChallenge.model.User;
import com.trungnh103.shapeChallenge.repository.RoleRepository;
import com.trungnh103.shapeChallenge.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class CustomUserDetailsServiceTest {
    @TestConfiguration
    static class CustomUserDetailsServiceTestContextConfiguration {
        @Bean
        public CustomUserDetailsService userDetailsService() {
            return new CustomUserDetailsService();
        }
    }

    @Autowired
    CustomUserDetailsService userDetailsService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setup() {
        User messi = new User("messi", "p@ssw0rd");
        messi.setRoles(Sets.newHashSet(Arrays.asList(new Role("ADMIN"))));
        User pep = new User("pep", null);
        pep.setRoles(Sets.newHashSet(Arrays.asList(new Role("ADMIN"))));
        User ronaldo = new User("ronaldo", null);
        ronaldo.setRoles(Sets.newHashSet(Arrays.asList(new Role("KID"))));
        List<User> users = Arrays.asList(messi, ronaldo, pep);

        Mockito.when(userRepository.findByUsername("messi")).thenReturn(messi);
        Mockito.when(userRepository.findByActive(true)).thenReturn(users);

    }

    @Test
    public void test_findUserByUsername() {
        User found = userDetailsService.findUserByUsername("messi");
        verifyFindUserByUsernameIsCalledOnce();
        assertThat(found.getUsername()).isEqualTo("messi");
    }

    @Test
    public void test_loadUserByUsername() {
        UserDetails messiDetails = userDetailsService.loadUserByUsername("messi");
        verifyFindUserByUsernameIsCalledOnce();
        String foundUsername = messiDetails.getUsername();
        List<String> authorities = messiDetails.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toList());
        assertThat(foundUsername).isEqualTo("messi");
        assertThat(authorities).contains("ADMIN");
    }

    @Test
    public void test_findAllActiveAdmins() {
        List<User> found = userDetailsService.findAllActiveAdmins();
        List<String> names = found.stream().map(user -> user.getUsername()).collect(Collectors.toList());
        verifyFindByActiveIsCalledOnce();
        assertThat(found.size()).isEqualTo(2);
        assertThat(names).contains("pep", "messi");
    }

    @Test
    public void test_findKids() {
        List<User> found = userDetailsService.findKids();
        List<String> names = found.stream().map(user -> user.getUsername()).collect(Collectors.toList());
        verifyFindByActiveIsCalledOnce();
        assertThat(found.size()).isEqualTo(1);
        assertThat(names).contains("ronaldo");
    }

    private void verifyFindUserByUsernameIsCalledOnce() {
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findByUsername("messi");
        Mockito.reset(userRepository);
    }

    private void verifyFindByActiveIsCalledOnce() {
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findByActive(true);
        Mockito.reset(userRepository);
    }
}
