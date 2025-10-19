package br.com.fiap.bookstorerestapi.service.impl;

import br.com.fiap.bookstorerestapi.dto.BookRecord;
import br.com.fiap.bookstorerestapi.entity.Author;
import br.com.fiap.bookstorerestapi.entity.Book;
import br.com.fiap.bookstorerestapi.exception.NotFoundException;
import br.com.fiap.bookstorerestapi.mapper.BookMapper;
import br.com.fiap.bookstorerestapi.repository.AuthorRepository;
import br.com.fiap.bookstorerestapi.repository.BookRepository;
import br.com.fiap.bookstorerestapi.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookRecord createBook(BookRecord bookRecord) {
        Author author = authorRepository.findById(bookRecord.authorId())
                .orElseThrow(() -> new NotFoundException("Autor não encontrado com o ID: " + bookRecord.authorId()));

        Book book = bookMapper.toEntity(bookRecord);
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toRecord(savedBook);
    }

    @Override
    public BookRecord getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado com o ID: " + id));
        return bookMapper.toRecord(book);
    }

    @Override
    public List<BookRecord> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toRecord)
                .collect(Collectors.toList());
    }

    @Override
    public BookRecord updateBook(Long id, BookRecord bookRecord) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado com o ID: " + id));

        Author author = authorRepository.findById(bookRecord.authorId())
                .orElseThrow(() -> new NotFoundException("Autor não encontrado com o ID: " + bookRecord.authorId()));

        existingBook.setTitle(bookRecord.title());
        existingBook.setIsbn(bookRecord.isbn());
        existingBook.setAuthor(author);

        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toRecord(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Livro não encontrado com o ID: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookRecord> getBooksByAuthorId(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new NotFoundException("Autor não encontrado com o ID: " + authorId);
        }
        return bookRepository.findByAuthorId(authorId).stream()
                .map(bookMapper::toRecord)
                .collect(Collectors.toList());
    }
}