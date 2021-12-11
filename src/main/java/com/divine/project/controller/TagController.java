package com.divine.project.controller;

import com.divine.project.model.Item;
import com.divine.project.model.Tag;
import com.divine.project.payload.responses.ApiResponse;
import com.divine.project.payload.responses.PagedResponse;
import com.divine.project.service.TagService;
import com.divine.project.service.serviceImpl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PutMapping("/add_item")
    public ResponseEntity<ApiResponse> addItemToTag(@RequestParam(name = "itemId") Long itemId,
                                               @RequestParam(name = "tag") String tagName){
        return new ResponseEntity<>(new ApiResponse(tagService.addItemToTag(tagName, itemId), String.format("Item added to '%s'", tagName)), HttpStatus.OK);
    }

    @GetMapping("/remove_item")
    public ResponseEntity<ApiResponse> removeItemFromTag(@RequestParam(name = "itemId") Long itemId,
                                               @RequestParam(name = "tag") String tagName){
        return new ResponseEntity<>(new ApiResponse(tagService.removeItemfromTag(tagName, itemId), String.format("Item removed from '%s'", tagName)), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tag>> getAllTags(){
        return new ResponseEntity<>(tagService.getTags(), HttpStatus.OK);
    }
}
