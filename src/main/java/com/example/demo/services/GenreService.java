package com.example.demo.services;

import com.example.demo.models.Genre;
import com.example.demo.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    GenreRepository genreRepository;

    public void update(Long id, Genre genre){
        Optional<Genre> genreData = genreRepository.findById(id);

        if (genreData.isPresent()){
            Genre _genre = genreData.get();
            //Update the genre
            _genre.setName(genre.getName());
            _genre.setDescription(genre.getDescription());
            genreRepository.save(_genre);
        }
    }
}
