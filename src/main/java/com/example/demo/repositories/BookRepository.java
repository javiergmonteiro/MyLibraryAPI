package com.example.demo.repositories;

import com.example.demo.models.Book;
import org.apache.juli.logging.Log;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    Book findByTitle(String title);

    void deleteById(Long id);
}
