package com.trungnh103.shapeChallenge.controller;

import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.Shape;
import com.trungnh103.shapeChallenge.service.CustomUserDetailsService;
import com.trungnh103.shapeChallenge.service.ShapeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/shapes")
@Api(tags = "Restful API for mobile app")
public class ApiController {
    @Autowired
    ShapeService shapeService;

    @Autowired
    private CustomUserDetailsService userService;

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<Category> getAllCategories() {
        return shapeService.getAllCategories();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> createShape(@RequestBody Shape shape) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        shape.setUsername(auth.getName());
        Shape createdShape = shapeService.createShape(shape);
        String area = shapeService.calculateArea(createdShape);
        Set possibleCategories = shapeService.getPossibleCategories(createdShape);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok(("Area: " + area + ", possible categories: " + possibleCategories));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Shape> updateShape(@PathVariable(value = "id") String shapeId, @RequestBody Shape shapeDetails) throws ChangeSetPersister.NotFoundException {
        Shape shape = shapeService.findById(shapeId).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        shape.setName(shapeDetails.getName());
        shape.setCategory(shapeDetails.getCategory());
        shape.setShapeProperties(shapeDetails.getShapeProperties());
        return ResponseEntity.ok(shapeService.updateShape(shape));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Shape> getSavedShapes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return shapeService.getSavedShapes(auth.getName());
    }
}
