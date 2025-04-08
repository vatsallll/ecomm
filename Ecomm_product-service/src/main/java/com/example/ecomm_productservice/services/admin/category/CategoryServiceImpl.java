package com.example.ecomm_productservice.services.admin.category;

import com.example.ecomm_productservice.dto.CategoryDto;
import com.example.ecomm_productservice.entity.Category;
import com.example.ecomm_productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createcategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setName (categoryDto.getName());
        category.setDescription (categoryDto.getDescription());

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}