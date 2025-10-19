package br.com.fiap.bookstorerestapi.service.impl;

import br.com.fiap.bookstorerestapi.dto.AuthorRecord;
import br.com.fiap.bookstorerestapi.entity.Author;
import br.com.fiap.bookstorerestapi.exception.NotFoundException;
import br.com.fiap.bookstorerestapi.mapper.AuthorMapper;
import br.com.fiap.bookstorerestapi.repository.AuthorRepository;
import br.com.fiap.bookstorerestapi.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorRecord createAuthor(AuthorRecord authorRecord) {
        Author author = authorMapper.toEntity(authorRecord);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toRecord(savedAuthor);
    }

    @Override
    public AuthorRecord getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + id));
        return authorMapper.toRecord(author);
    }

    @Override
    public List<AuthorRecord> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toRecord)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorRecord updateAuthor(Long id, AuthorRecord authorRecord) {
        // Busca o autor primeiro; se não existir, lança a exceção.
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + id));

        // Atualiza os dados do autor existente
        existingAuthor.setName(authorRecord.name());
        existingAuthor.setEmail(authorRecord.email());

        Author updatedAuthor = authorRepository.save(existingAuthor);
        return authorMapper.toRecord(updatedAuthor);
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new NotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
}