package com.example.demo.services;

import com.example.demo.Exceptions.OutOfStockException;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import com.example.demo.models.User;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.GenreRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.BookAPIService.GoogleBookApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GoogleBookApiService googleBookApiService;

    public void sell(Book book, User user) throws OutOfStockException {
        if (book.getQuantity() > 0) {
            user.getOwnedBooks().add(book);
            userRepository.save(user);
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.save(book);
        } else {
            throw new OutOfStockException("The book requested is out of stock");
        }
    }

    public Book save(Book book) {
        List<Author> unverified_authors = book.getAuthors();
        List<Genre> unverified_genres = book.getGenres();

        List<Author> verified_authors = new ArrayList<>();
        List<Genre> verified_genres = new ArrayList<>();

        for (int i = 0; i < unverified_authors.size(); i++) {
            //System.out.println(unverified_authors.size());
            //System.out.println(i);
            Author unverified_author = unverified_authors.get(i);
            Author author = authorRepository.findByName(unverified_author.getName());
            if (author == null) {
                author = authorRepository.save(unverified_author);
            }
            verified_authors.add(author);
        }

        for (int i = 0; i < unverified_genres.size(); i++) {
            //System.out.println(unverified_authors.size());
            //System.out.println(i);
            Genre unverified_genre = unverified_genres.get(i);
            Genre genre = genreRepository.findByName(unverified_genre.getName());
            if (genre == null) {
                genre = genreRepository.save(unverified_genre);
            }
            verified_genres.add(genre);
        }

        book.setAuthors(verified_authors);
        book.setGenres(verified_genres);
        bookRepository.save(book);
        return book;
    }

    public BookSearchResult searchBy(String title, String genre) {
        BookSearchResult bookSearchResult = new BookSearchResult();
        String apiQuery;
        if (title != null && genre != null) {
            apiQuery = title + "+" + genre;
            List<Book> books = new ArrayList<>();
            for (Book book : bookRepository.findByTitleContaining(title)) {
                if (!books.contains(book) && book != null) {
                    books.add(book);
                }
            }
            for (Book book : bookRepository.findByGenresNameContaining(genre)) {
                if (!books.contains(book) && book != null) {
                    books.add(book);
                }
            }
            if (books != null && !books.isEmpty()) {
                bookSearchResult.setResults(books);
            }
        } else if (title != null) {
            apiQuery = title;
            List<Book> books = bookRepository.findByTitleContaining(title);
            if (books != null) {
                System.out.println(books);
                bookSearchResult.setResults(books);
            }
        } else if (genre != null) {
            apiQuery = genre;
            List<Book> books = bookRepository.findByGenresNameContaining(genre);
            if (books != null) {
                bookSearchResult.setResults(books);
            }
        } else {
            apiQuery = null;
            List<Book> books = (List<Book>) bookRepository.findAll();
            if (books != null) {
                bookSearchResult.setResults(books);
            }
        }
        try {
            List<Book> api_books = googleBookApiService.fetchAPIBooks(apiQuery);
            bookSearchResult.setSimilar_books(api_books);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return bookSearchResult;
    }

    public void updateBook(Long id, Book book) {
        Optional<Book> bookData = bookRepository.findById(id);
        List<Genre> verifiedGenres = new ArrayList<>();
        List<Author> verifiedAuthors = new ArrayList<>();
        if (bookData.isPresent()) {
            Book _book = bookData.get();
            List<Author> authors = _book.getAuthors();
            List<Genre> genres = _book.getGenres();
            for (Author _author : authors){
                Optional<Author> author = authorRepository.findById(_author.getId());
                if (author.isPresent()){
                    verifiedAuthors.add(author.get());
                }
            }
            for (Genre _genre : genres){
                Optional<Genre> genre = genreRepository.findById(_genre.getId());
                if (genre.isPresent()){
                    verifiedGenres.add(genre.get());
                }
            }
            //Update the book
            _book.setTitle(book.getTitle());
            _book.setSynopsis(book.getSynopsis());
            _book.setIsbn(book.getIsbn());
            _book.setAuthors(book.getAuthors());
            _book.setGenres(book.getGenres());
            bookRepository.save(_book);
        }
    }
}


