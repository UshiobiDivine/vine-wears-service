package com.divine.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tag")
public class Tag extends DateAudit{

    @Column
    private String title;

    @Column
    private String routeName;

//    @Column
//    private String imageUrl;

    @Column
    private String linkUrl;


    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    private List<Item> items;

    public Tag(String title, String routeName, String linkUrl) {
        this.title = title;
        this.routeName = routeName;
        this.linkUrl = linkUrl;
    }
}
