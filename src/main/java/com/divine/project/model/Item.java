package com.divine.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@Table(name = "item")
public class Item extends DateAudit{

    @Column
    private String name;

    @Column
    private String imageUrl;

    @Column
    private String price;

    @Column
    private String description;

    @Column
    private String quantityAvailable;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "item_category", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "item_tag", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "item_sizes", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "size_id"))
    private List<Size> sizes;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "item_colors", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "color_id"))
    private List<Color> colors;
}
