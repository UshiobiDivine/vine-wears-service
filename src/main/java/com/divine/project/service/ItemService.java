package com.divine.project.service;

import com.divine.project.model.Item;
import com.divine.project.payload.requests.AddItemRequest;
import com.divine.project.payload.requests.UpdateItemRequest;
import com.divine.project.payload.responses.ItemResponse;
import com.divine.project.payload.responses.PagedResponse;

import java.util.List;

public interface ItemService {
    ItemResponse addItem(AddItemRequest addItemRequest);
    ItemResponse updateItem(Long id, UpdateItemRequest updateItemRequest);
    boolean deleteItem(Long id);
    ItemResponse getItem(Long id);
    PagedResponse<Item> getAllItems(int page, int size);
    PagedResponse<Item> getAllItemsByCategory(String catName, int page, int size);
    PagedResponse<Item> getAllItemsByTag(String tagName, int page, int size);
    List<PagedResponse<Item>> getListOfAllItemsByTag();
    PagedResponse<Item> searchItemsByName(String searchWord, int page, int size);

}
