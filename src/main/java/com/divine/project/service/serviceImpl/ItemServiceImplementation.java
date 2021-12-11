package com.divine.project.service.serviceImpl;

import com.divine.project.exception.BadRequestException;
import com.divine.project.model.*;
import com.divine.project.payload.requests.AddItemRequest;
import com.divine.project.payload.requests.UpdateItemRequest;
import com.divine.project.payload.responses.ItemResponse;
import com.divine.project.payload.responses.PagedResponse;
import com.divine.project.repository.*;
import com.divine.project.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImplementation implements ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ColorRepository colorRepository;
    private SizeRepository sizeRepository;
    private TagRepository tagRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ItemServiceImplementation(ItemRepository itemRepository, UserRepository userRepository, CategoryRepository categoryRepository, ColorRepository colorRepository, SizeRepository sizeRepository, TagRepository tagRepository, ModelMapper modelMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ItemResponse addItem(AddItemRequest addItemRequest) {

        Item item = handleSizeColorCategories(addItemRequest);

        if (item.getName()!=null) {

            Item savedItem = itemRepository.save(item);
            ItemResponse itemResponse = modelMapper.map(savedItem, ItemResponse.class);
            if (savedItem != null) {
                return itemResponse;
            }
        }
        throw new BadRequestException("Could not add item");

    }

    @Override
    public ItemResponse updateItem(Long id, UpdateItemRequest updateItemRequest) {

        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent()) {
            AddItemRequest addItemRequest = modelMapper.map(updateItemRequest, AddItemRequest.class);
            Item item = handleSizeColorCategories(addItemRequest);

            Item itemToEdit = itemOptional.get();
            itemToEdit.setName(item.getName());
            itemToEdit.setPrice(item.getPrice());
            itemToEdit.setDescription(item.getDescription());
            itemToEdit.setImageUrl(item.getImageUrl());
            itemToEdit.setColors(item.getColors());
            itemToEdit.setSizes(item.getSizes());
            itemToEdit.setCategories(item.getCategories());

            Item saveItem = itemRepository.save(itemToEdit);
            ItemResponse itemResponse = modelMapper.map(saveItem, ItemResponse.class);
            return itemResponse;
        }
        throw new BadRequestException("Could not find item to update");
    }

    @Override
    public boolean deleteItem(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent()) {
//            Item item = itemOptional.get();
            itemRepository.deleteById(id);
            return true;
        }
        throw new BadRequestException("Could not find item to delete");
    }

    @Override
    public ItemResponse getItem(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent()) {
            ItemResponse itemResponse = modelMapper.map(itemOptional.get(), ItemResponse.class);
            return itemResponse;
        }
        throw new BadRequestException("Could not find item");
    }

    @Override
    public PagedResponse<Item> getAllItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Item> allByPage = itemRepository.findAll(pageable);

        return makePage(allByPage);
    }

    @Override
    public PagedResponse<Item> getAllItemsByCategory(String catName, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Category categoryByTitle = categoryRepository.findCategoryByTitle(catName);

        if (categoryByTitle==null){
            throw new BadRequestException("Could not find category");
        }

        Page<Item> allByPage = itemRepository.findAllByCategories(categoryByTitle, pageable);

        return makePage(allByPage);
    }

    @Override
    public PagedResponse<Item> getAllItemsByTag(String tagName, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Tag tagByTitle = tagRepository.findByTitle(tagName);

        if (tagByTitle==null){
            throw new BadRequestException("Could not find tag");
        }
        System.out.println("TAAAAAAAGGGGGGGGGGG "+ tagByTitle);
        Page<Item> allByPage = itemRepository.findAllByTags(tagByTitle, pageable);

        return makePage(allByPage);
    }

    @Override
    public List<PagedResponse<Item>> getListOfAllItemsByTag() {
        return List.of(getAllItemsByTag("Just For You", 0, 10),
                getAllItemsByTag("Flash Deals", 0, 10),
                getAllItemsByTag("New Arrivals", 0, 10),
                getAllItemsByTag("Top Selections", 0, 10));
    }

    @Override
    public PagedResponse<Item> searchItemsByName(String searchWord, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Item> itemsPage = itemRepository.findAllByNameContains(searchWord, pageable);

        return makePage(itemsPage);
    }

    public Item handleSizeColorCategories(AddItemRequest addItemRequest) {

        Item item = new Item();

        if (addItemRequest.getCategories() != null) {

            List<Category> categoryList = new ArrayList<>();

            addItemRequest.getCategories().forEach((category) ->
                    {
                        Category categoryByTitle = categoryRepository.findCategoryByTitle(category.getTitle());
                        if (categoryByTitle != null) {
                            categoryList.add(categoryByTitle);
                            categoryByTitle.getItems().add(item);
                            item.setCategories(categoryList);
                        } else {
                            throw new BadRequestException("Could not find category");
                        }
                    }
            );

        }

        List<Color> colors = addItemRequest.getColors();

        if (colors != null) {
            List<Color> colorList = new ArrayList<>();
            for (Color color : colors) {
                Optional<Color> colorByName = colorRepository.findByName(color.getName().toLowerCase());

                colorByName.ifPresent(colorList::add);

                if (colorByName.isEmpty()){
                    Color newColor = new Color(color.getName().toLowerCase());
                    colorList.add(newColor);
                    colorRepository.save(newColor);
                    System.out.println("A new color was added");
                }
            }
            item.setColors(colorList);

        }

        List<Size> sizes = addItemRequest.getSizes();

        if (sizes != null) {
            List<Size> sizeList = new ArrayList<>();
            for (Size size : sizes) {
                Optional<Size> sizeByName = sizeRepository.findByName(size.getName().toLowerCase());

                sizeByName.ifPresent(sizeList::add);

                if (sizeByName.isEmpty()){
                    Size newSize = new Size(size.getName().toLowerCase());
                    sizeList.add(newSize);
                    sizeRepository.save(newSize);
                    System.out.println("A new size was added");
                }
            }
            item.setSizes(sizeList);

            item.setDescription(addItemRequest.getDescription());
            item.setName(addItemRequest.getName());
            item.setImageUrl(addItemRequest.getImageUrl());
            item.setPrice(addItemRequest.getPrice());
            item.setQuantityAvailable(addItemRequest.getQuantityAvailable());
        }
        return item;
    }

    public PagedResponse<Item> makePage(Page<Item> page){
        List<Item> content = page.getNumberOfElements() == 0 ?
                Collections.emptyList() : page.getContent();

        return new PagedResponse<>(content, page.getNumber(),
                page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isLast());
    }
}
