package br.com.fiap.bookstorerestapi.service;

import br.com.fiap.bookstorerestapi.dto.AuthorRecord;
import java.util.List;

public interface AuthorService {
    AuthorRecord createAuthor(AuthorRecord authorRecord);
    AuthorRecord getAuthorById(Long id);
    List<AuthorRecord> getAllAuthors();
    AuthorRecord updateAuthor(Long id, AuthorRecord authorRecord);
    void deleteAuthor(Long id);
}
