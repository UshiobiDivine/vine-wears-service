package com.divine.project.service;

import com.divine.project.model.Tag;

import java.util.List;

public interface TagService {
    boolean addItemToTag(String tagTitle, Long itemId);
    boolean removeItemfromTag(String tagTitle, Long itemId);
    List<Tag> getTags();
}
