package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.Author;
import com.example.demo.entity.Category;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.CategoryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class GoogleBooksService {

    private final WebClient webClient;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    public GoogleBooksService(BookRepository bookRepository,
                              AuthorRepository authorRepository,
                              CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.objectMapper = new ObjectMapper();
        this.webClient = WebClient.create("https://www.googleapis.com");
    }

    @Async
    public CompletableFuture<Book> fetchAndSaveBookByIsbn(String isbn) {
        System.out.println("Début import asynchrone pour ISBN: " + isbn);
        System.out.println("Thread: " + Thread.currentThread().getName());

        try {
            String response = webClient.get()
                    .uri("/books/v1/volumes?q=isbn:" + isbn)
                    .retrieve()
                    .onStatus(status -> status.value() == 429,
                              response2 -> Mono.error(new RuntimeException("Trop de requêtes, attendez 1 minute")))
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            JsonNode items = root.get("items");

            if (items == null || !items.isArray() || items.size() == 0) {
                System.out.println("Aucun livre trouvé pour ISBN: " + isbn);
                return CompletableFuture.completedFuture(null);
            }

            JsonNode volumeInfo = items.get(0).get("volumeInfo");

            Book book = new Book();
            book.setTitle(volumeInfo.get("title").asText());

            if (volumeInfo.has("description")) {
                book.setDescription(volumeInfo.get("description").asText());
            }

            if (volumeInfo.has("publishedDate")) {
                try {
                    String dateStr = volumeInfo.get("publishedDate").asText();
                    if (dateStr.length() == 4) {
                        book.setPublicationDate(LocalDate.parse(dateStr + "-01-01"));
                    } else if (dateStr.length() == 7) {
                        book.setPublicationDate(LocalDate.parse(dateStr + "-01"));
                    } else {
                        book.setPublicationDate(LocalDate.parse(dateStr));
                    }
                } catch (Exception e) {
                    book.setPublicationDate(LocalDate.now());
                }
            }

            if (volumeInfo.has("industryIdentifiers")) {
                for (JsonNode identifier : volumeInfo.get("industryIdentifiers")) {
                    if (identifier.get("type").asText().equals("ISBN_13")) {
                        book.setIsbn(identifier.get("identifier").asText());
                        break;
                    }
                }
            }

            book.setPrice(0.0);

            List<Author> authors = new ArrayList<>();
            if (volumeInfo.has("authors")) {
                for (JsonNode authorNode : volumeInfo.get("authors")) {
                    String authorName = authorNode.asText();
                    String[] nameParts = authorName.split(" ", 2);
                    Author author = new Author();
                    author.setFirstName(nameParts.length > 0 ? nameParts[0] : "");
                    author.setLastName(nameParts.length > 1 ? nameParts[1] : "");
                    author.setBiography("Importé depuis Google Books");
                    author.setNationality("Inconnue");
                    authors.add(authorRepository.save(author));
                }
            }
            book.setAuthors(authors);

            if (volumeInfo.has("categories")) {
                String categoryName = volumeInfo.get("categories").get(0).asText();
                Category category = categoryRepository.findAll().stream()
                        .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                        .findFirst()
                        .orElseGet(() -> {
                            Category newCat = new Category();
                            newCat.setName(categoryName);
                            newCat.setDescription("Importé depuis Google Books");
                            return categoryRepository.save(newCat);
                        });
                book.setCategory(category);
            }

            Book savedBook = bookRepository.save(book);
            System.out.println("Livre importé avec succès: " + savedBook.getTitle());

            return CompletableFuture.completedFuture(savedBook);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'import: " + e.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }
}