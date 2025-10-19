package br.com.fiap.bookstorerestapi.service.impl;

import br.com.fiap.bookstorerestapi.dto.BookRecord;
import br.com.fiap.bookstorerestapi.entity.Author;
import br.com.fiap.bookstorerestapi.entity.Book;
import br.com.fiap.bookstorerestapi.exception.NotFoundException;
import br.com.fiap.bookstorerestapi.mapper.BookMapper;
import br.com.fiap.bookstorerestapi.repository.AuthorRepository;
import br.com.fiap.bookstorerestapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void testCreateBook_QuandoAutorNaoExiste_DeveLancarExcecao() {
        BookRecord record = new BookRecord(null, "Título", "isbn", 99L);
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.createBook(record));
        verify(authorRepository).findById(99L);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testCreateBook_QuandoAutorExiste_DeveSalvarLivro() {
        // Arrange
        BookRecord recordToSave = new BookRecord(null, "Título", "isbn", 1L);
        Author author = new Author();
        Book bookEntity = new Book();
        Book savedBookEntity = new Book();
        BookRecord expectedRecord = new BookRecord(1L, "Título", "isbn", 1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookMapper.toEntity(recordToSave)).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenReturn(savedBookEntity);
        when(bookMapper.toRecord(savedBookEntity)).thenReturn(expectedRecord);

        // Act
        BookRecord result = bookService.createBook(recordToSave);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(bookRepository).save(bookEntity);
    }

    @Test
    void testGetBookById_QuandoNaoEncontrado_DeveLancarExcecao() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.getBookById(99L));
        verify(bookRepository).findById(99L);
    }

    @Test
    void testGetBookById_QuandoEncontrado_DeveRetornarLivro() {
        // Arrange
        Book bookEntity = new Book();
        BookRecord expectedRecord = new BookRecord(1L, "Título", "isbn", 1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(bookMapper.toRecord(bookEntity)).thenReturn(expectedRecord);

        BookRecord result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(expectedRecord, result);
        verify(bookRepository).findById(1L);
    }

    @Test
    void testDeleteBook_QuandoNaoEncontrado_DeveLancarExcecao() {
        when(bookRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookService.deleteBook(99L));
        verify(bookRepository).existsById(99L);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteBook_QuandoEncontrado_DeveDeletarComSucesso() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void testUpdateBook_QuandoLivroNaoEncontrado_DeveLancarExcecao() {
        BookRecord recordToUpdate = new BookRecord(99L, "Título", "isbn", 1L);
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.updateBook(99L, recordToUpdate));
        verify(bookRepository).findById(99L);
        verify(authorRepository, never()).findById(anyLong());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testUpdateBook_QuandoAutorNaoEncontrado_DeveLancarExcecao() {
        BookRecord recordToUpdate = new BookRecord(1L, "Título", "isbn", 99L);
        Book existingBook = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.updateBook(1L, recordToUpdate));
        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(99L);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testGetBooksByAuthorId_QuandoAutorNaoExiste_DeveLancarExcecao() {
        when(authorRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookService.getBooksByAuthorId(99L));
        verify(authorRepository).existsById(99L);
        verify(bookRepository, never()).findByAuthorId(anyLong());
    }

    @Test
    void testGetBooksByAuthorId_QuandoAutorExiste_DeveRetornarListaDeLivros() {
        Book book1 = new Book();
        Book book2 = new Book();
        List<Book> bookList = List.of(book1, book2);

        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findByAuthorId(1L)).thenReturn(bookList);
        when(bookMapper.toRecord(any(Book.class)))
                .thenReturn(new BookRecord(1L, "Livro 1", "isbn1", 1L))
                .thenReturn(new BookRecord(2L, "Livro 2", "isbn2", 1L));

        List<BookRecord> result = bookService.getBooksByAuthorId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(authorRepository).existsById(1L);
        verify(bookRepository).findByAuthorId(1L);
    }
}