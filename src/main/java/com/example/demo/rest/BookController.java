package com.example.demo.rest;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import com.example.demo.models.User;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.GenreRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.BookAPIService.GoogleBookApiService;
import com.example.demo.services.BookSearchResult;
import com.example.demo.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GoogleBookApiService restService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) throws URISyntaxException {
        Book createdBook = bookService.save(book);
        System.out.println(createdBook);
        if (createdBook == null){
            return ResponseEntity.notFound().build();
        } else{
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdBook.getId())
                    .toUri();
            return ResponseEntity.created(uri)
                    .body(createdBook);
        }
    }

    @GetMapping("")
    public BookSearchResult list(@RequestParam(required = false) String title, @RequestParam(required = false) String genre){
        return bookService.searchBy(title, genre);
    }

    @GetMapping("{id}")
    public Optional<Book> getOneBook(@PathVariable Long id){
        return bookRepository.findById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book){
        Optional<Book> bookData = bookRepository.findById(id);

        if (bookData.isPresent()){
            Book _book = bookData.get();
            List<Author> authors = _book.getAuthors();
            List<Genre> genres = _book.getGenres();
            for (Author _author : authors){
                Optional<Author> author = authorRepository.findById(_author.getId());
                if (!author.isPresent()){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            for (Genre _genre : genres){
                Optional<Genre> genre = genreRepository.findById(_genre.getId());
                if (!genre.isPresent()){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            //Update the book
            _book.setTitle(book.getTitle());
            _book.setSynopsis(book.getSynopsis());
            _book.setIsbn(book.getIsbn());
            _book.setAuthors(book.getAuthors());
            _book.setGenres(book.getGenres());
            return new ResponseEntity<>(bookRepository.save(_book), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        bookRepository.deleteById(id);
    }

    @PostMapping("{id}/sell/{clientId}")
    public ResponseEntity<Object> sell(@PathVariable Long id, @PathVariable Integer clientId){
        Optional<Book> _book = bookRepository.findById(id);
        Optional<User> _user = userRepository.findById(clientId);
        if (_book.isPresent() && _user.isPresent()){
            Book book = _book.get();
            User user = _user.get();
            if (book.getQuantity() <= 0){
                return ResponseEntity.notFound().build();
            }
            else{
                book.setQuantity(book.getQuantity()-1);
                user.getOwnedBooks().add(book);
                userRepository.save(user);
                bookRepository.save(book);
                return ResponseEntity.ok().build();
            }
        }
        else{
            return ResponseEntity.notFound().build();
        }

    }
}
