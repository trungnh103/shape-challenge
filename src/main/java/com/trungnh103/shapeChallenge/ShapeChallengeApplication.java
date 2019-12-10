package com.trungnh103.shapeChallenge;

import com.trungnh103.shapeChallenge.common.Constants;
import com.trungnh103.shapeChallenge.model.Role;
import com.trungnh103.shapeChallenge.model.User;
import com.trungnh103.shapeChallenge.repository.RoleRepository;
import com.trungnh103.shapeChallenge.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class ShapeChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShapeChallengeApplication.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        return args -> {

            Role adminRole = roleRepository.findByRole(Constants.ROLE_ADMIN);
            if (adminRole == null) {
                Role newAdminRole = new Role(Constants.ROLE_ADMIN);
                adminRole = roleRepository.save(newAdminRole);
            }

            Role kidRole = roleRepository.findByRole(Constants.ROLE_KID);
            if (kidRole == null) {
                Role newKidRole = new Role(Constants.ROLE_KID);
                roleRepository.save(newKidRole);
            }

            User firstAdmin = userRepository.findByUsername(Constants.FIRST_ADMIN);
            if (firstAdmin == null) {
                firstAdmin = new User(Constants.FIRST_ADMIN, bCryptPasswordEncoder.encode(Constants.INITIAL_PASSWORD));
                firstAdmin.setFullName(Constants.FIRST_ADMIN_FULL_NAME);
                firstAdmin.setPhoneNumber(Constants.FIRST_ADMIN_PHONE_NUMBER);
                firstAdmin.setEmail(Constants.FIRST_ADMIN_EMAIL);
                firstAdmin.setActive(true);
                firstAdmin.setRoles(new HashSet<>(Arrays.asList(adminRole)));
                userRepository.save(firstAdmin);
            }
        };

    }

}
