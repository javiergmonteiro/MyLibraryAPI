package com.example.demo.rest;

import com.example.demo.models.Author;
import com.example.demo.models.Genre;
import com.example.demo.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/author")

public class AuthorController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @Autowired
    private AuthorRepository authorRepository;

    @PostMapping("")
    public void save(@Valid @RequestBody Author author){
        authorRepository.save(author);
    }

    @GetMapping("")
    public List<Author> list(){
        return (List<Author>) authorRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Author> getOneAuthor(@PathVariable("id") long id){
        Optional<Author> _author = authorRepository.findById(id);
        if (_author.isPresent()){
            Author author = _author.get();
            return new ResponseEntity<>(author, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable("id") long id, @RequestBody Author author){
        Optional<Author> authorData = authorRepository.findById(id);

        if (authorData.isPresent()){
            Author _author = authorData.get();
            //Update the author
            _author.setName(author.getName());
            return new ResponseEntity<>(authorRepository.save(_author), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id){
        authorRepository.deleteById(id);
    }
}