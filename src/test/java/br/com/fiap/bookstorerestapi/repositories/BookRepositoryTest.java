package br.com.fiap.bookstorerestapi.repositories;

import br.com.fiap.bookstorerestapi.entity.Author;
import br.com.fiap.bookstorerestapi.entity.Book;
import br.com.fiap.bookstorerestapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testFindByAuthorId_ShouldReturnBooksForAuthor() {
        // Arrange
        Author author = new Author("George Orwell", "go@email.com");
        entityManager.persist(author);

        Book book1 = new Book("1984", "12345", author);
        entityManager.persist(book1);

        Book book2 = new Book("Animal Farm", "67890", author);
        entityManager.persist(book2);

        entityManager.flush();

        List<Book> books = bookRepository.findByAuthorId(author.getId());

        assertThat(books).hasSize(2).contains(book1, book2);
    }
}