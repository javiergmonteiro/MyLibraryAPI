package com.example.demo;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookCreationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void bookShouldBeCreated() throws Exception {

        String uri = "http://localhost:"+port+"/book";

        Book book = new Book();
        Genre genre = new Genre();
        genre.setName("Test Genre");
        genre.setDescription("This is a test genre");
        Author author = new Author();
        author.setName("Test Author");

        book.setTitle("test book");
        book.setSynopsis("this is a test book");
        book.getGenres().add(genre);
        book.getAuthors().add(author);
        book.setQuantity(10);
        book.setIsbn("TEST01010101");

        Book post_response = restTemplate.postForObject(uri, book, Book.class);

        Book book_response = restTemplate.getForObject(uri+"/"+post_response.getId().toString(), Book.class);

        assertThat(book.getTitle().equals(book_response.getTitle()));

        restTemplate.delete(uri+"/"+post_response.getId().toString());

    }
}
