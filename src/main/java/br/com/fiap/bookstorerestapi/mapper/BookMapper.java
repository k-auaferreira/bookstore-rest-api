package br.com.fiap.bookstorerestapi.mapper;

import br.com.fiap.bookstorerestapi.dto.BookRecord;
import br.com.fiap.bookstorerestapi.entity.Book;

public interface BookMapper {

    Book toEntity(BookRecord dto);
    BookRecord toRecord(Book entity);

}
