package com.divine.project.payload.requests;

import com.divine.project.model.Category;
import com.divine.project.model.Color;
import com.divine.project.model.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class AddItemRequest {

    @NotBlank
    private String name;

    private String imageUrl;

//    @JsonIgnore
//    private String image;

    @NotBlank
    private String price;

    private String description;

    @NotBlank
    private String quantityAvailable;

    @NotBlank
    private List<Category> categories;

    private List<Color> colors;

    private List<Size> sizes;
}
