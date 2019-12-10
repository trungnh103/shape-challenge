package com.trungnh103.shapeChallenge.controller;

import com.trungnh103.shapeChallenge.common.Constants;
import com.trungnh103.shapeChallenge.model.User;
import com.trungnh103.shapeChallenge.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private CustomUserDetailsService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView showAdminList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> admins = userService.findAllActiveAdmins();
        List<User> adminsExceptLoggedInUser = admins.stream().filter(user -> !user.getUsername().equals(auth.getName())).collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("admins", adminsExceptLoggedInUser);
        modelAndView.setViewName("admins");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView showCreateAdminForm() {
        ModelAndView modelAndView = new ModelAndView();
        User admin = new User(null, null);
        modelAndView.addObject("admin", admin);
        modelAndView.setViewName("create-admin");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createAdmin(@Valid User admin) {
        admin.setPassword(Constants.INITIAL_PASSWORD);
        userService.saveUser(admin, Constants.ROLE_ADMIN);
        return "redirect:";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteAdmin(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
        userService.delete(id);
        return "redirect:/admins";
    }
}
