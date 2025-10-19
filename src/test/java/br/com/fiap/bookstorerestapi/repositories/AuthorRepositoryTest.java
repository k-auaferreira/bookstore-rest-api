package br.com.fiap.bookstorerestapi.repositories;

import br.com.fiap.bookstorerestapi.entity.Author;
import br.com.fiap.bookstorerestapi.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testSaveAndFindById_ShouldReturnAuthor() {
        Author author = new Author("J.R.R. Tolkien", "tolkien@middleearth.com");
        entityManager.persistAndFlush(author);

        Author found = authorRepository.findById(author.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo(author.getName());
    }
}