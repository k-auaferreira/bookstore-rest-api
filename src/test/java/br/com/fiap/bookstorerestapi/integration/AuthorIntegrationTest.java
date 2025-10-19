package br.com.fiap.bookstorerestapi.integration;

import br.com.fiap.bookstorerestapi.dto.AuthorRecord;
import br.com.fiap.bookstorerestapi.entity.Author;
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
class AuthorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void testCreateAuthorAndFindById() throws Exception {
        AuthorRecord requestRecord = new AuthorRecord(null, "Integration Test Author", "integration@test.com");

        String responseContent = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        AuthorRecord createdAuthor = objectMapper.readValue(responseContent, AuthorRecord.class);

        mockMvc.perform(get("/authors/{id}", createdAuthor.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test Author"));
    }

    @Test
    void testGetAllAuthors() throws Exception {
        Author author1 = new Author("Author One", "one@test.com");
        Author author2 = new Author("Author Two", "two@test.com");
        authorRepository.saveAll(List.of(author1, author2));

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Author One"))
                .andExpect(jsonPath("$[1].name").value("Author Two"));
    }

    @Test
    void testUpdateAuthor() throws Exception {
        Author existingAuthor = authorRepository.save(new Author("Original Name", "original@test.com"));
        AuthorRecord updatedRecord = new AuthorRecord(null, "Updated Name", "updated@test.com");

        mockMvc.perform(put("/authors/{id}", existingAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingAuthor.getId()))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@test.com"));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        Author authorToDelete = authorRepository.save(new Author("To Be Deleted", "delete@test.com"));
        Long authorId = authorToDelete.getId();

        mockMvc.perform(delete("/authors/{id}", authorId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/authors/{id}", authorId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAuthorById_NotFound() throws Exception {
        mockMvc.perform(get("/authors/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}