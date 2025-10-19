package br.com.fiap.bookstorerestapi.mapper;

import br.com.fiap.bookstorerestapi.dto.AuthorRecord;
import br.com.fiap.bookstorerestapi.entity.Author;

public interface AuthorMapper {

    Author toEntity(AuthorRecord dto);
    AuthorRecord toRecord(Author entity);

}
