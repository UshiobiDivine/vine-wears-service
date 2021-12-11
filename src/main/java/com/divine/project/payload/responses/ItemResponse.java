package com.divine.project.payload.responses;

import com.divine.project.model.Category;
import com.divine.project.model.Color;
import com.divine.project.model.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Data
public class ItemResponse {

    private Long id;

    private String name;

    private String imageUrl;

    private String price;

    private String description;

    private String quantityAvailable;

    private List<Category> categories;

    private List<Size> sizes;

    private List<Color> colors;
}
