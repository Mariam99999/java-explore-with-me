package com.example.mainservice.controller;

import com.example.mainservice.model.Category;
import com.example.mainservice.model.NewCategoryDto;
import com.example.mainservice.model.NewUserRequest;
import com.example.mainservice.model.UserDto;
import com.example.mainservice.service.CategoryService;
import com.example.mainservice.service.UserService;
import com.sun.xml.txw2.annotation.XmlNamespace;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Category addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto){
        return categoryService.addCategory(newCategoryDto);
    }
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable Long catId){
        categoryService.deleteCategory(catId);
    }
    @PatchMapping("/{catId}")
    Category updateCategory(@PathVariable Long catId, @RequestBody @Valid NewCategoryDto newCategoryDto){
       return categoryService.updateCategory(catId,newCategoryDto);
    }
}
