package com.example.demo.rest;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.GenreRepository;
import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
    public List<Book> list(){
        return (List<Book>) bookRepository.findAll();
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
}
