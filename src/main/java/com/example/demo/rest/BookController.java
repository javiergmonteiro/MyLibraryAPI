package com.example.demo.rest;

import com.example.demo.Exceptions.OutOfStockException;
import com.example.demo.models.Book;
import com.example.demo.models.User;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.GenreRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.BookAPIService.GoogleBookApiService;
import com.example.demo.services.BookSearchResult;
import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
        bookService.updateBook(id, book);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        bookRepository.deleteById(id);
    }

    @PostMapping("{id}/sell/{clientId}")
    public ResponseEntity<Object> sell(@PathVariable Long id, @PathVariable Integer clientId) throws OutOfStockException {
        Optional<Book> _book = bookRepository.findById(id);
        Optional<User> _user = userRepository.findById(clientId);
        if (_book.isPresent() && _user.isPresent()) {
            try {
                bookService.sell(_book.get(), _user.get());
                return ResponseEntity.ok().build();
            } catch (OutOfStockException e) {
                return ResponseEntity.badRequest().build();
            }
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
