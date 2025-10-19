package br.com.fiap.bookstorerestapi.mapper;

import br.com.fiap.bookstorerestapi.dto.BookRecord;
import br.com.fiap.bookstorerestapi.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public Book toEntity(BookRecord dto) {
        if (dto == null) {
            return null;
        }

        Book book = new Book();
        book.setId(dto.id());
        book.setTitle(dto.title());
        book.setIsbn(dto.isbn());

        return book;
    }

    @Override
    public BookRecord toRecord(Book entity) {
        if (entity == null) {
            return null;
        }

        // Verifica se o autor não é nulo para evitar NullPointerException.
        Long authorId = (entity.getAuthor() != null) ? entity.getAuthor().getId() : null;

        return new BookRecord(
                entity.getId(),
                entity.getTitle(),
                entity.getIsbn(),
                authorId
        );
    }
}
