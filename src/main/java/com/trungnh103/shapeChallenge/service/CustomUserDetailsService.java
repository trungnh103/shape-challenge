package com.trungnh103.shapeChallenge.service;

import com.trungnh103.shapeChallenge.model.Role;
import com.trungnh103.shapeChallenge.model.User;
import com.trungnh103.shapeChallenge.repository.RoleRepository;
import com.trungnh103.shapeChallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user, String role) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole(role);
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public List<User> findAllActiveAdmins() {
        List<User> admins = userRepository.findByActive(true);
        return admins.stream().filter(user -> isAdmin(user)).collect(Collectors.toList());
    }

    private boolean isAdmin(User user) {
        for (Role role : user.getRoles()) {
            if ("ADMIN".equals(role.getRole())) {
                return true;
            }
        }
        return false;
    }

    public void delete(String id) throws ChangeSetPersister.NotFoundException {
        User admin = userRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        admin.setActive(false);
        userRepository.save(admin);
    }

    public List<User> findKids() {
        List<User> kids = userRepository.findByActive(true);
        return kids.stream().filter(user -> !isAdmin(user)).collect(Collectors.toList());
    }
}
