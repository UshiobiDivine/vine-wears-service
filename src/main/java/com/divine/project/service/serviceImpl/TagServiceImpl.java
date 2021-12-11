package com.divine.project.service.serviceImpl;

import com.divine.project.exception.BadRequestException;
import com.divine.project.model.Item;
import com.divine.project.model.Tag;
import com.divine.project.repository.ItemRepository;
import com.divine.project.repository.TagRepository;
import com.divine.project.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;
    private ItemRepository itemRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, ItemRepository itemRepository) {
        this.tagRepository = tagRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public boolean addItemToTag(String tagTitle, Long itemId) {
        Tag tagByTitle = tagRepository.findByTitle(tagTitle);
        Optional<Item> itemById = itemRepository.findById(itemId);
        if (tagByTitle==null || itemById==null){
            throw new BadRequestException("Tag or Item not found");
        }

        List<Tag> tagList = itemById.get().getTags();
        List<Item> items = tagByTitle.getItems();
        if (!tagList.contains(tagByTitle) && !items.contains(itemById.get())){
            tagList.add(tagByTitle);
            items.add(itemById.get());
            tagRepository.save(tagByTitle);
            itemRepository.save(itemById.get());
            return true;
        }
        throw new BadRequestException("Item is either already in tag or otherwise");

    }

    @Override
    public boolean removeItemfromTag(String tagTitle, Long itemId) {
        Tag tagByTitle = tagRepository.findByTitle(tagTitle);
        Optional<Item> itemById = itemRepository.findById(itemId);
        if (tagByTitle==null || itemById==null){
            throw new BadRequestException("Tag or Item not found");
        }

        List<Tag> tagList = itemById.get().getTags();
        List<Item> items = tagByTitle.getItems();

        if (tagList.contains(tagByTitle) && items.contains(itemById.get())){
            tagList.remove(tagByTitle);
            items.remove(itemById.get());
            tagRepository.save(tagByTitle);
            itemRepository.save(itemById.get());
            return true;
        }
        throw new BadRequestException("Item is either not in tag or otherwise");

    }

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }
}
