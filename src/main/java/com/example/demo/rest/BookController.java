package com.example.demo.rest;

import com.example.demo.models.Book;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("")
    public ResponseEntity<Book> create(@RequestBody Book book) throws URISyntaxException {
        Book createdBook = bookRepository.save(book);
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
    /*public void save(@RequestBody Book book){
        bookRepository.save(book);
    }*/

    @GetMapping("")
    public List<Book> list(){
        return (List<Book>) bookRepository.findAll();
    }

    @GetMapping("{id}")
    public Book getOneBook(@PathVariable Integer id){
        return bookRepository.findById(id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id){
        bookRepository.deleteById(id);
    }
}
