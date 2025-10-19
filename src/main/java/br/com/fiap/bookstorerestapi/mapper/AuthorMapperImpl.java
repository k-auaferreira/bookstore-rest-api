package br.com.fiap.bookstorerestapi.mapper;

import br.com.fiap.bookstorerestapi.dto.AuthorRecord;
import br.com.fiap.bookstorerestapi.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapperImpl implements AuthorMapper {
    @Override
    public Author toEntity(AuthorRecord dto) {
        Author author = new Author();
        author.setId(dto.id());
        author.setName(dto.name());
        author.setEmail(dto.email());
        return author;
    }

    @Override
    public AuthorRecord toRecord(Author entity) {
        return new AuthorRecord(entity.getId(), entity.getName(), entity.getEmail());
    }
}
