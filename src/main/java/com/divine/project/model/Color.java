package com.divine.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "colors")
public class Color extends DateAudit {

    @Column
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "colors", fetch = FetchType.EAGER)
    private List<Item> items;

    public Color(String name) {
        this.name = name;
    }
}
