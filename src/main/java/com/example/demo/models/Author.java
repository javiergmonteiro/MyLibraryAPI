package com.example.demo.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "The name cannot be null")
    private String name;
    @OneToMany(targetEntity = Book.class)
    private List<Book> books;

    public Author(){}

    public Author(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Author[id=%d, name='%s']",
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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
