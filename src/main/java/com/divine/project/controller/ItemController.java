package com.divine.project.controller;

import com.divine.project.model.Item;
import com.divine.project.payload.requests.AddItemRequest;
import com.divine.project.payload.requests.UpdateItemRequest;
import com.divine.project.payload.responses.ApiResponse;
import com.divine.project.payload.responses.ItemResponse;
import com.divine.project.payload.responses.PagedResponse;
import com.divine.project.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/admin")
    public ResponseEntity<ItemResponse> addItem(@RequestBody AddItemRequest addItemRequest){
        System.out.println("THE ADD ITEM REQUEST IS  ------"+addItemRequest);

        return new ResponseEntity<>(itemService.addItem(addItemRequest), HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ItemResponse> updateItem(@Valid @RequestBody UpdateItemRequest updateItemRequest,
                           @PathVariable(name = "id") Long id){
        return new ResponseEntity<> (itemService.updateItem(id, updateItemRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(itemService.getItem(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PagedResponse<Item>> allItem(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(value = "size", required = false, defaultValue = "30") Integer size){
        return new ResponseEntity<>(itemService.getAllItems(page, size), HttpStatus.OK);
    }

    @GetMapping("/by_category")
    public ResponseEntity<PagedResponse<Item>> itemsByCategory(@RequestParam(name = "category") String category,
                                                               @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                               @RequestParam(value = "size", required = false, defaultValue = "30") Integer size){
        return new ResponseEntity<>(itemService.getAllItemsByCategory(category, page, size), HttpStatus.OK);
    }

    @GetMapping("/by_tag")
    public ResponseEntity<PagedResponse<Item>> itemsByTag(@RequestParam(name = "tag") String tag,
                                                          @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                          @RequestParam(value = "size", required = false, defaultValue = "30") Integer size){
        return new ResponseEntity<>(itemService.getAllItemsByTag(tag, page, size), HttpStatus.OK);
    }

    @GetMapping("/all_by_tag")
    public ResponseEntity<List<PagedResponse<Item>>> listOfAllItemsByTag(){
        return new ResponseEntity<>(itemService.getListOfAllItemsByTag(), HttpStatus.OK);
    }


    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(new ApiResponse(itemService.deleteItem(id), "Item deleted"), HttpStatus.OK);
    }




}
