package com.divine.project.repository;

import com.divine.project.model.Category;
import com.divine.project.model.Item;
import com.divine.project.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByTitle(String title);

}
