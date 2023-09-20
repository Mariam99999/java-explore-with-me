package com.example.mainservice.service;

import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.CategoryMapper;
import com.example.mainservice.model.Category;
import com.example.mainservice.model.CategoryDto;
import com.example.mainservice.model.NewCategoryDto;
import com.example.mainservice.storage.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public Category addCategory(NewCategoryDto newCategoryDto){
        return categoryRepository.save(categoryMapper.mapToCategory(newCategoryDto));
    }
    public void deleteCategory(Long id){
        categoryRepository.findById(id).orElseThrow(NotFoundException::new);
        categoryRepository.deleteById(id);
    }
    public Category updateCategory(Long id,NewCategoryDto newCategoryDto){
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setName(newCategoryDto.getName());
       return categoryRepository.save(category);
    }
    public List<Category> getCategories(int from, int size){
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepository.findAll(pageable).getContent();
    }
    public Category getCategoryById(Long catId){
        return categoryRepository.findById(catId).orElseThrow(NotFoundException::new);
    }

}
