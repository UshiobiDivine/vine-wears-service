package com.divine.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sizes")
public class Size extends DateAudit{

    @Column
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "sizes", fetch = FetchType.EAGER)
    private List<Item> items;

    public Size(String name) {
        this.name = name;
    }
}
