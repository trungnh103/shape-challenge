package com.trungnh103.shapeChallenge.controller;

import com.trungnh103.shapeChallenge.dto.ShapeDto;
import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.Shape;
import com.trungnh103.shapeChallenge.model.User;
import com.trungnh103.shapeChallenge.service.CategoryService;
import com.trungnh103.shapeChallenge.service.CustomUserDetailsService;
import com.trungnh103.shapeChallenge.service.ShapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/shapes")
public class ShapeController {
    @Autowired
    private ShapeService shapeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomUserDetailsService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView showShapeList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("shapes", shapeService.findAll());
        modelAndView.setViewName("shapes");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView showCreateShapeForm() {
        ModelAndView modelAndView = new ModelAndView();
        ShapeDto shape = new ShapeDto();
        modelAndView.addObject("shape", shape);
        List<Category> categories = categoryService.findAll();
        modelAndView.addObject("categories", categories);
        List<User> kids = userService.findKids();
        modelAndView.addObject("kids", kids);
        modelAndView.setViewName("create-shape");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createShape(@Valid ShapeDto shapeDto) {
        Shape shape = shapeService.convertFromDto(shapeDto);
        shapeService.createShape(shape);
        return "redirect:";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView showEditShapeForm(@PathVariable String id) {
        Shape shape = shapeService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        ShapeDto shapeDto = shapeService.convertToDto(shape);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("shape", shapeDto);
        List<Category> categories = categoryService.findAll();
        modelAndView.addObject("categories", categories);
        List<User> kids = userService.findKids();
        modelAndView.addObject("kids", kids);
        modelAndView.setViewName("edit-shape");
        return modelAndView;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String editShape(@Valid ShapeDto shapeDto, @PathVariable String id) {
        Shape shape = shapeService.convertFromDto(shapeDto);
        shape.setId(id);
        shapeService.createShape(shape);
        return "redirect:/shapes";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteShape(@PathVariable String id) {
        shapeService.delete(id);
        return "redirect:/shapes";
    }
}
