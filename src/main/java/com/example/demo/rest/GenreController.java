package com.example.demo.rest;

import com.example.demo.models.Genre;
import com.example.demo.repositories.GenreRepository;
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
@RequestMapping("api/v1/genre")
public class GenreController {

    @Autowired
    GenreRepository genreRepository;

    @PostMapping("")
    public ResponseEntity<Genre> create(@Valid @RequestBody Genre genre) throws URISyntaxException {
        Genre createdGenre = genreRepository.save(genre);
        //System.out.println(createdGenre);
        if (createdGenre == null){
            return ResponseEntity.notFound().build();
        } else{
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdGenre.getId())
                    .toUri();
            return ResponseEntity.created(uri)
                    .body(createdGenre);
        }
    }

    @GetMapping("")
    public List<Genre> list(){
        return (List<Genre>) genreRepository.findAll();
    }

    @GetMapping("{id}")
    public Optional<Genre> getOneGenre(@PathVariable Long id){
        return genreRepository.findById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable("id") long id, @RequestBody Genre genre){
        Optional<Genre> genreData = genreRepository.findById(id);

        if (genreData.isPresent()){
            Genre _genre = genreData.get();
            //Update the genre
            _genre.setName(genre.getName());
            _genre.setDescription(genre.getDescription());
            return new ResponseEntity<>(genreRepository.save(_genre), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        genreRepository.deleteById(id);
    }

}
