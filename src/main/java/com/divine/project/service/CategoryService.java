package com.divine.project.service;


import com.divine.project.model.Category;
import com.divine.project.model.Item;
import com.divine.project.payload.responses.PagedResponse;

import java.util.List;

public interface CategoryService{

    List<Category> getAllCategories();
    PagedResponse<Item> getAllItemsInCategory(String categoryName, int page, int size);

}
