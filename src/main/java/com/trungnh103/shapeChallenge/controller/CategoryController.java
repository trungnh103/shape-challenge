package com.trungnh103.shapeChallenge.controller;

import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.CategoryCondition;
import com.trungnh103.shapeChallenge.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/categories")
@Api(tags = "Category Management")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView showCategoryList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categories", categoryService.findAll());
        modelAndView.setViewName("categories");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView showCreateCategoryForm() {
        ModelAndView modelAndView = new ModelAndView();
        Category category = new Category();
        modelAndView.addObject("category", category);
        modelAndView.setViewName("create-category");
        return modelAndView;
    }

    @RequestMapping(value = "/create", params = {"addRequirement"})
    public ModelAndView addRequirement(final Category category) {
        if (category.getRequirements() == null) category.setRequirements(new ArrayList<>());
        category.getRequirements().add(new String());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("category", category);
        modelAndView.setViewName("create-category");
        return modelAndView;
    }

    @RequestMapping(value = "/create", params = {"removeRequirement"})
    public ModelAndView removeRequirement(final Category category, final HttpServletRequest req) {
        final Integer requirementIndex = Integer.valueOf(req.getParameter("removeRequirement"));
        category.getRequirements().remove(requirementIndex.intValue());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("category", category);
        modelAndView.setViewName("create-category");
        return modelAndView;
    }

    @RequestMapping(value = "/create", params = {"addCondition"})
    public ModelAndView addCondition(final Category category) {
        if (category.getConditions() == null) category.setConditions(new ArrayList<>());
        category.getConditions().add(new CategoryCondition(null, null));
        ModelAndView modelAndView = new ModelAndView();
        List<Category> categories = categoryService.findAll();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("category", category);
        modelAndView.setViewName("create-category");
        return modelAndView;
    }

    @RequestMapping(value = "/create", params = {"removeCondition"})
    public ModelAndView removeCondition(final Category category, final HttpServletRequest req) {
        final Integer conditionIndex = Integer.valueOf(req.getParameter("removeCondition"));
        category.getConditions().remove(conditionIndex.intValue());
        ModelAndView modelAndView = new ModelAndView();
        List<Category> categories = categoryService.findAll();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("category", category);
        modelAndView.setViewName("create-category");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createCategory(@Valid Category category) {
        categoryService.save(category);
        return "redirect:";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
