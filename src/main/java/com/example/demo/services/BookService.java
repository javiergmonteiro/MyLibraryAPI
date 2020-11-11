package com.example.demo.services;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    GenreRepository genreRepository;

    public Book save(Book book){
        List<Author> unverified_authors = book.getAuthors();
        List<Genre> unverified_genres = book.getGenres();

        List<Author> verified_authors = new ArrayList<>();
        List<Genre> verified_genres = new ArrayList<>();

        for (int i=0;i < unverified_authors.size(); i++){
            //System.out.println(unverified_authors.size());
            //System.out.println(i);
            Author unverified_author = unverified_authors.get(i);
            Author author = authorRepository.findByName(unverified_author.getName());
            if (author == null){
                author = authorRepository.save(unverified_author);
            }
            verified_authors.add(author);
        }

        for (int i=0;i < unverified_genres.size(); i++){
            //System.out.println(unverified_authors.size());
            //System.out.println(i);
            Genre unverified_genre = unverified_genres.get(i);
            Genre genre = genreRepository.findByName(unverified_genre.getName());
            if (genre == null){
                genre = genreRepository.save(unverified_genre);
            }
            verified_genres.add(genre);
        }

        book.setAuthors(verified_authors);
        book.setGenres(verified_genres);
        bookRepository.save(book);
        return book;
    }
}

