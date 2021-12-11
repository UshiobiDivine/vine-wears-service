package com.divine.project.service.serviceImpl;

import com.divine.project.model.Category;
import com.divine.project.model.Item;
import com.divine.project.payload.responses.PagedResponse;
import com.divine.project.repository.CategoryRepository;
import com.divine.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public PagedResponse<Item> getAllItemsInCategory(String categoryName, int page, int size) {
        Category categoryByTitle = categoryRepository.findCategoryByTitle(categoryName);
//        categoryByTitle.
        return null;
    }
}
