package com.example.demo.services;

import com.example.demo.models.Book;

import java.util.List;

public class BookSearchResult{

    List<Book> results;
    List<Book> similar_books;

    public List<Book> getResults() {
        return results;
    }

    public void setResults(List<Book> results) {
        this.results = results;
    }

    public List<Book> getSimilar_books() {
        return similar_books;
    }

    public void setSimilar_books(List<Book> similar_books) {
        this.similar_books = similar_books;
    }
}