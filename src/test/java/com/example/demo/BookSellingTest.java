package com.example.demo;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import com.example.demo.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class BookSellingTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void bookShouldDecreasedInQuantityAfterSell() throws Exception {

        final Integer quantity = 10;

        String book_uri = "http://localhost:"+port+"/book";
        String user_uri = "http://localhost:"+port+"/user/register";

        User user = new User();

        user.setUsername("testuser");
        user.setPassword("123456");

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
        book.setQuantity(quantity);
        book.setIsbn("TEST01010101");

        User user_response = restTemplate.postForObject(user_uri, user, User.class);

        Book post_response = restTemplate.postForObject(book_uri, book, Book.class);

        Book book_response = restTemplate.getForObject(book_uri+"/"+post_response.getId().toString(), Book.class);

        restTemplate.postForObject(book_uri+"/"+book_response.getId().toString()+"/sell/"+user_response.getId(), null, Void.class);

        Book quantity_decreased_book_response = restTemplate.getForObject(book_uri+"/"+book_response.getId().toString(), Book.class);

        assertThat(quantity_decreased_book_response.getQuantity() == (quantity-1));

        restTemplate.delete(book_uri+"/"+post_response.getId().toString());
        restTemplate.delete(user_uri+"/"+user_response.getId());

    }
}
