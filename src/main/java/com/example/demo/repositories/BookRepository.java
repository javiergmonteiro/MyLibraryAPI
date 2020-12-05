package com.example.demo.repositories;

import com.example.demo.models.Book;
import org.apache.juli.logging.Log;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findByTitleContaining(String title);
    List<Book> findByGenresNameContaining(String genre);
    void deleteById(Long id);
}
