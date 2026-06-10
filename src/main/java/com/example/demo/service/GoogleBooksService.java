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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class GoogleBooksService {

    private final WebClient openLibraryClient;
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
        this.openLibraryClient = WebClient.create("https://openlibrary.org");
    }

    @Async
    public CompletableFuture<Book> fetchAndSaveBookByIsbn(String isbn) {
        System.out.println("📚 Début import depuis Open Library pour ISBN: " + isbn);
        System.out.println("Thread: " + Thread.currentThread().getName());

        try {
            Book book = fetchFromOpenLibrary(isbn);

            if (book == null) {
                System.out.println("❌ Aucun livre trouvé pour ISBN: " + isbn);
                return CompletableFuture.completedFuture(null);
            }

            System.out.println("✅ Livre importé avec succès: " + book.getTitle());
            return CompletableFuture.completedFuture(book);

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'import: " + e.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }

    private Book fetchFromOpenLibrary(String isbn) {
        try {
            // Appel à l'API Open Library
            String response = openLibraryClient.get()
                    .uri("/api/books?bibkeys=ISBN:" + isbn + "&jscmd=data&format=json")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            String key = "ISBN:" + isbn;

            if (!root.has(key)) {
                System.out.println("⚠️ ISBN non trouvé sur Open Library: " + isbn);
                return null;
            }

            JsonNode bookData = root.get(key);

            // Création du livre
            Book book = new Book();
            book.setTitle(bookData.get("title").asText());
            book.setIsbn(isbn);
            book.setPrice(0.0); // Open Library n'a pas de prix

            // Description
            if (bookData.has("subtitle")) {
                book.setDescription(bookData.get("subtitle").asText());
            } else if (bookData.has("notes")) {
                book.setDescription(bookData.get("notes").asText());
            } else {
                book.setDescription("Importé depuis Open Library");
            }

            // Date de publication
            if (bookData.has("publish_date")) {
                try {
                    String dateStr = bookData.get("publish_date").asText();
                    // Nettoie la date (ex: "2000" -> "2000-01-01")
                    if (dateStr.matches("\\d{4}")) {
                        book.setPublicationDate(LocalDate.parse(dateStr + "-01-01"));
                    } else if (dateStr.matches("\\d{4}-\\d{2}")) {
                        book.setPublicationDate(LocalDate.parse(dateStr + "-01"));
                    } else {
                        book.setPublicationDate(LocalDate.now());
                    }
                } catch (Exception e) {
                    book.setPublicationDate(LocalDate.now());
                }
            } else {
                book.setPublicationDate(LocalDate.now());
            }

            // Gestion des auteurs
            List<Author> authors = new ArrayList<>();
            if (bookData.has("authors")) {
                for (JsonNode authorNode : bookData.get("authors")) {
                    String authorName = authorNode.get("name").asText();
                    Author author = createOrGetAuthor(authorName);
                    authors.add(author);
                }
            }
            book.setAuthors(authors);

            // Gestion des catégories (sujets)
            if (bookData.has("subjects")) {
                for (JsonNode subjectNode : bookData.get("subjects")) {
                    String subjectName = subjectNode.get("name").asText();
                    Category category = findOrCreateCategory(subjectName);
                    book.setCategory(category);
                    break; // Prend seulement la première catégorie
                }
            }

            // Si pas de catégorie, en créer une par défaut
            if (book.getCategory() == null) {
                Category defaultCategory = findOrCreateCategory("Général");
                book.setCategory(defaultCategory);
            }

            // Sauvegarde en base
            Book savedBook = bookRepository.save(book);
            System.out.println("📖 Livre sauvegardé: " + savedBook.getTitle());

            return savedBook;

        } catch (Exception e) {
            System.err.println("Erreur Open Library pour ISBN " + isbn + ": " + e.getMessage());
            return null;
        }
    }

    private Author createOrGetAuthor(String fullName) {
        // Vérifie si l'auteur existe déjà
        Author existingAuthor = authorRepository.findAll().stream()
                .filter(a -> (a.getFirstName() + " " + a.getLastName()).equalsIgnoreCase(fullName))
                .findFirst()
                .orElse(null);

        if (existingAuthor != null) {
            return existingAuthor;
        }

        // Crée un nouvel auteur
        Author author = new Author();
        String[] nameParts = fullName.split(" ", 2);
        author.setFirstName(nameParts.length > 0 ? nameParts[0] : fullName);
        author.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        author.setBiography("Importé depuis Open Library");
        author.setNationality("Inconnue");

        return authorRepository.save(author);
    }

    private Category findOrCreateCategory(String name) {
        // Limite la longueur du nom (sinon trop long)
        String cleanName = name.length() > 100 ? name.substring(0, 100) : name;

        Category existingCategory = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase(cleanName))
                .findFirst()
                .orElse(null);

        if (existingCategory != null) {
            return existingCategory;
        }

        Category newCategory = new Category();
        newCategory.setName(cleanName);
        newCategory.setDescription("Importé depuis Open Library");

        return categoryRepository.save(newCategory);
    }
}