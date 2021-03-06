package com.example.demo.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "The name cannot be null")
    @Column(unique = true)
    private String name;
    @NotBlank(message = "The description cannot be null")
    private String description;

    public Genre() {}

    @Override
    public String toString() {
        return String.format(
                "Genre[id=%d, name='%s']",
                id, name
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
