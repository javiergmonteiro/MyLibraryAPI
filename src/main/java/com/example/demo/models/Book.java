package com.example.demo.models;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String isbn;
    @NotBlank(message = "The title cannot be null")
    private String title;
    @NotBlank(message = "The synopsis cannot be null")
    @Lob
    @Column(length = 100000)
    private String synopsis;

    @ManyToMany(targetEntity = Genre.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    List<Genre> genres;
    @ManyToMany(targetEntity = Author.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    List<Author> authors;

    @Override
    public String toString() {
        return String.format(
                "Book[id=%d, title='%s', isbn='%s']",
                id, title, isbn
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
