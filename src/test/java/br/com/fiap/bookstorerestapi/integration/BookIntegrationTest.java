package br.com.fiap.bookstorerestapi.integration;

import br.com.fiap.bookstorerestapi.dto.BookRecord;
import br.com.fiap.bookstorerestapi.entity.Author;
import br.com.fiap.bookstorerestapi.entity.Book;
import br.com.fiap.bookstorerestapi.repository.AuthorRepository;
import br.com.fiap.bookstorerestapi.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private Author savedAuthor;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        savedAuthor = authorRepository.save(new Author("Test Author", "author@test.com"));
    }

    @Test
    void testCreateBook() throws Exception {
        BookRecord requestRecord = new BookRecord(null, "A New Book", "123456789", savedAuthor.getId());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("A New Book"))
                .andExpect(jsonPath("$.authorId").value(savedAuthor.getId()));
    }

    @Test
    void testGetBookById() throws Exception {
        Book savedBook = bookRepository.save(new Book("Existing Book", "987654321", savedAuthor));

        mockMvc.perform(get("/books/{id}", savedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedBook.getId()))
                .andExpect(jsonPath("$.title").value("Existing Book"));
    }

    @Test
    void testGetAllBooks() throws Exception {
        Book book1 = new Book("Book One", "111", savedAuthor);
        Book book2 = new Book("Book Two", "222", savedAuthor);
        bookRepository.saveAll(List.of(book1, book2));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Book One"))
                .andExpect(jsonPath("$[1].title").value("Book Two"));
    }

    @Test
    void testUpdateBook() throws Exception {
        Book existingBook = bookRepository.save(new Book("Original Title", "555", savedAuthor));
        BookRecord updatedRecord = new BookRecord(null, "Updated Title", "555-new", savedAuthor.getId());

        mockMvc.perform(put("/books/{id}", existingBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingBook.getId()))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.isbn").value("555-new"));
    }

    @Test
    void testDeleteBook() throws Exception {
        Book bookToDelete = bookRepository.save(new Book("To Be Deleted", "000", savedAuthor));
        Long bookId = bookToDelete.getId();

        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBookById_NotFound() throws Exception {
        mockMvc.perform(get("/books/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}