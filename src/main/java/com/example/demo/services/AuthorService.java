package com.example.demo.services;

import com.example.demo.models.Author;
import com.example.demo.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    public void update(Long id, Author author){
        Optional<Author> authorData = authorRepository.findById(id);

        if (authorData.isPresent()){
            Author _author = authorData.get();
            //Update the author
            _author.setName(author.getName());
        }
    }

}
