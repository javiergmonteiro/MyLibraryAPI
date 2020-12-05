package com.example.demo.services.BookAPIService;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import com.example.demo.services.BookSearchResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleAPIBooks{

    @JsonProperty("kind")
    String kind;
    @JsonProperty("totalItems")
    Integer totalItems;
    @JsonProperty("items")
    List<Item> items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Item{

    String kind;
    String id;
    VolumeInfo volumeInfo;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class VolumeInfo{

    @JsonProperty("title")
    String title;
    @JsonProperty("subtitle")
    String subtitle;
    @JsonProperty("authors")
    List<String> authors;
    @JsonProperty("description")
    String description;
    @JsonProperty("categories")
    List<String> categories;
    @JsonProperty("industryIdentifiers")
    List<IndustryIdentifiers> industryIdentifiers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<IndustryIdentifiers> getIndustryIdentifiers() {
        return industryIdentifiers;
    }

    public void setIndustryIdentifiers(List<IndustryIdentifiers> industryIdentifiers) {
        this.industryIdentifiers = industryIdentifiers;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class IndustryIdentifiers{

    String type;
    String identifier;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}

@Service
public class GoogleBookApiService {

    String api_key = "AIzaSyBUcacKLVhS4PvBOfqsbzp_QwhShbEtbNs";

    RestTemplate restTemplate = new RestTemplate();
    JsonFactory factory = new JsonFactory();
    ObjectMapper mapper = new ObjectMapper(factory);

    public List<Book> fetchAPIBooks(String query) throws URISyntaxException, JsonProcessingException {

        List<Book> api_books = new ArrayList<>();
        if (query != null){
            String book_url = "https://www.googleapis.com/books/v1/volumes?q="+query+"&key="+api_key;
            System.out.println(book_url);
            URI uri = new URI(book_url);
            String response = restTemplate.getForObject(uri, String.class);
            GoogleAPIBooks bookResponse = mapper.readValue(response, GoogleAPIBooks.class);
            List<Item> items = bookResponse.getItems();
            for(Item item : items){
                Book book = new Book();
                List<Genre> genres = new ArrayList<>();
                List<Author> authors = new ArrayList<>();
                try{
                    for (String category: item.getVolumeInfo().getCategories()){
                        Genre genre = new Genre();
                        genre.setName(category);
                        genres.add(genre);
                    }
                    book.setGenres(genres);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                try{
                    for (String _author: item.getVolumeInfo().getAuthors()){
                        //System.out.println(_author);
                        Author author = new Author();
                        author.setName(_author);
                        authors.add(author);
                    }
                    book.setAuthors(authors);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                book.setTitle(item.getVolumeInfo().getTitle() +" "+item.getVolumeInfo().getSubtitle());
                try{
                    IndustryIdentifiers isbn = item.getVolumeInfo().getIndustryIdentifiers().get(0);
                    book.setIsbn(isbn.getType()+isbn.getIdentifier());
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                book.setSynopsis(item.getVolumeInfo().getDescription());
                book.setQuantity(0);
                api_books.add(book);
            }
        }
        return api_books;
    }
}
