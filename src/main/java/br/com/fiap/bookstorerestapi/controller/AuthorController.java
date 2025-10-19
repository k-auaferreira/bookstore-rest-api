package br.com.fiap.bookstorerestapi.controller;

import br.com.fiap.bookstorerestapi.dto.AuthorRecord;
import br.com.fiap.bookstorerestapi.dto.BookRecord;
import br.com.fiap.bookstorerestapi.service.AuthorService;
import br.com.fiap.bookstorerestapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<AuthorRecord> createAuthor(@RequestBody AuthorRecord authorRecord) {
        AuthorRecord createdAuthor = authorService.createAuthor(authorRecord);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AuthorRecord>> getAllAuthors() {
        List<AuthorRecord> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorRecord> getAuthorById(@PathVariable Long id) {
        AuthorRecord author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorRecord> updateAuthor(@PathVariable Long id, @RequestBody AuthorRecord authorRecord) {
        AuthorRecord updatedAuthor = authorService.updateAuthor(id, authorRecord);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookRecord>> getBooksByAuthor(@PathVariable Long id) {
        List<BookRecord> books = bookService.getBooksByAuthorId(id);
        return ResponseEntity.ok(books);
    }
}
