package com.example.ecomm_productservice.services.admin.category;

import com.example.ecomm_productservice.dto.CategoryDto;
import com.example.ecomm_productservice.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createcategory(CategoryDto categoryDto);

    List<Category> getAllCategories();
}
