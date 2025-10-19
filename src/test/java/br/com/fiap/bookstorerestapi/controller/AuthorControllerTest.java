package br.com.fiap.bookstorerestapi.controller;

import br.com.fiap.bookstorerestapi.dto.AuthorRecord;
import br.com.fiap.bookstorerestapi.dto.BookRecord;
import br.com.fiap.bookstorerestapi.exception.GlobalExceptionHandler;
import br.com.fiap.bookstorerestapi.exception.NotFoundException;
import br.com.fiap.bookstorerestapi.service.AuthorService;
import br.com.fiap.bookstorerestapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthorService authorService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testCreateAuthor() throws Exception {
        AuthorRecord requestRecord = new AuthorRecord(null, "Test Author", "test@author.com");
        AuthorRecord responseRecord = new AuthorRecord(1L, "Test Author", "test@author.com");

        when(authorService.createAuthor(any(AuthorRecord.class))).thenReturn(responseRecord);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Author"));
    }

    @Test
    void testGetAllAuthors() throws Exception {
        AuthorRecord author1 = new AuthorRecord(1L, "Author One", "one@author.com");
        AuthorRecord author2 = new AuthorRecord(2L, "Author Two", "two@author.com");
        List<AuthorRecord> authorList = List.of(author1, author2);

        when(authorService.getAllAuthors()).thenReturn(authorList);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Author One"))
                .andExpect(jsonPath("$[1].name").value("Author Two"));
    }

    @Test
    void testGetAuthorById_Success() throws Exception {
        AuthorRecord responseRecord = new AuthorRecord(1L, "Found Author", "found@author.com");
        when(authorService.getAuthorById(1L)).thenReturn(responseRecord);

        mockMvc.perform(get("/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Found Author"));
    }

    @Test
    void testGetAuthorById_NotFound() throws Exception {
        when(authorService.getAuthorById(99L)).thenThrow(new NotFoundException("Autor não encontrado"));

        mockMvc.perform(get("/authors/{id}", 99L))
                .andExpect(status().isNotFound());
    }



    @Test
    void testUpdateAuthor() throws Exception {
        AuthorRecord requestRecord = new AuthorRecord(null, "Updated Author", "updated@author.com");
        AuthorRecord responseRecord = new AuthorRecord(1L, "Updated Author", "updated@author.com");

        when(authorService.updateAuthor(eq(1L), any(AuthorRecord.class))).thenReturn(responseRecord);

        mockMvc.perform(put("/authors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Author"));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        doNothing().when(authorService).deleteAuthor(1L);

        mockMvc.perform(delete("/authors/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetBooksByAuthor() throws Exception {
        BookRecord book1 = new BookRecord(10L, "Book A", "111", 1L);
        BookRecord book2 = new BookRecord(11L, "Book B", "222", 1L);
        List<BookRecord> bookList = List.of(book1, book2);

        when(bookService.getBooksByAuthorId(1L)).thenReturn(bookList);

        mockMvc.perform(get("/authors/{id}/books", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book A"));
    }
}