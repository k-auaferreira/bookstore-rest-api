package br.com.fiap.bookstorerestapi.service;

import br.com.fiap.bookstorerestapi.dto.BookRecord;
import java.util.List;

public interface BookService {
    BookRecord createBook(BookRecord bookRecord);
    BookRecord getBookById(Long id);
    List<BookRecord> getAllBooks();
    BookRecord updateBook(Long id, BookRecord bookRecord);
    void deleteBook(Long id);
    List<BookRecord> getBooksByAuthorId(Long authorId);
}
