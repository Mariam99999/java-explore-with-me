package com.example.mainservice.service;

import com.example.mainservice.exception.ConflictException;
import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.CategoryMapper;
import com.example.mainservice.model.Category;
import com.example.mainservice.model.NewCategoryDto;
import com.example.mainservice.storage.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Category addCategory(NewCategoryDto newCategoryDto) {

        return tryToSave(categoryMapper.mapToCategory(newCategoryDto));
    }

    public void deleteCategory(Long id) {
        categoryRepository.findById(id).orElseThrow(NotFoundException::new);
        try {

            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException();
        }
    }

    public Category updateCategory(Long id, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setName(newCategoryDto.getName());
        return tryToSave(category);
    }


    public List<Category> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepository.findAll(pageable).getContent();
    }

    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(NotFoundException::new);
    }

    private Category tryToSave(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException();
        }
    }

}
