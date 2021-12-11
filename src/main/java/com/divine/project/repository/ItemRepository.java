package com.divine.project.repository;

import com.divine.project.model.Category;
import com.divine.project.model.Item;
import com.divine.project.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByNameContains(String searchWord, Pageable pageable);

    Page<Item> findAllByCategories(Category category, Pageable pageable);

    Page<Item> findAllByTags(Tag tag, Pageable pageable);


}
