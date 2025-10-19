package br.com.fiap.bookstorerestapi.controller;

import br.com.fiap.bookstorerestapi.dto.BookRecord;
import br.com.fiap.bookstorerestapi.exception.GlobalExceptionHandler;
import br.com.fiap.bookstorerestapi.exception.NotFoundException;
import br.com.fiap.bookstorerestapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testCreateBook_ComDadosValidos_DeveRetornarStatus201() throws Exception {
        BookRecord requestRecord = new BookRecord(null, "O Senhor dos Anéis", "978-0618640157", 1L);
        BookRecord responseRecord = new BookRecord(1L, "O Senhor dos Anéis", "978-0618640157", 1L);

        when(bookService.createBook(any(BookRecord.class))).thenReturn(responseRecord);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("O Senhor dos Anéis"));
    }

    @Test
    void testGetBookById_QuandoLivroExiste_DeveRetornarStatus200() throws Exception {
        BookRecord responseRecord = new BookRecord(1L, "O Senhor dos Anéis", "978-0618640157", 1L);
        when(bookService.getBookById(1L)).thenReturn(responseRecord);

        mockMvc.perform(get("/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetBookById_QuandoLivroNaoExiste_DeveRetornarStatus404() throws Exception {
        when(bookService.getBookById(99L)).thenThrow(new NotFoundException("Livro não encontrado com o ID: 99"));

        mockMvc.perform(get("/books/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllBooks_DeveRetornarListaDeLivrosEStatus200() throws Exception {
        BookRecord bookRecord = new BookRecord(1L, "O Senhor dos Anéis", "978-0618640157", 1L);
        when(bookService.getAllBooks()).thenReturn(List.of(bookRecord));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("O Senhor dos Anéis"));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookRecord requestRecord = new BookRecord(null, "The Lord of The Rings", "978-0618640157", 1L);
        BookRecord responseRecord = new BookRecord(1L, "The Lord of The Rings", "978-0618640157", 1L);

        when(bookService.updateBook(eq(1L), any(BookRecord.class))).thenReturn(responseRecord);

        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Lord of The Rings"));
    }

    @Test
    void testDeleteBook_QuandoLivroExiste_DeveRetornarStatus204() throws Exception {
        doNothing().when(bookService).deleteBook(anyLong());

        mockMvc.perform(delete("/books/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}