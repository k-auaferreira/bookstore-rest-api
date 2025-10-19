package br.com.fiap.bookstorerestapi.service.impl;

import br.com.fiap.bookstorerestapi.dto.AuthorRecord;
import br.com.fiap.bookstorerestapi.entity.Author;
import br.com.fiap.bookstorerestapi.exception.NotFoundException;
import br.com.fiap.bookstorerestapi.mapper.AuthorMapper;
import br.com.fiap.bookstorerestapi.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    // Teste de caminho triste (exceção)
    @Test
    void testGetAuthorById_QuandoNaoEncontrado_DeveLancarExcecao() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.getAuthorById(1L));
        verify(authorRepository).findById(1L);
    }

    // Teste de caminho feliz
    @Test
    void testGetAuthorById_QuandoEncontrado_DeveRetornarAuthor() {
        Author author = new Author();
        AuthorRecord record = new AuthorRecord(1L, "Nome", "email@test.com");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.toRecord(author)).thenReturn(record);

        AuthorRecord result = authorService.getAuthorById(1L);

        assertNotNull(result);
        assertEquals("Nome", result.name());
        verify(authorRepository).findById(1L);
    }

    @Test
    void testGetAllAuthors_DeveRetornarListaDeAutores() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = List.of(author1, author2);
        when(authorRepository.findAll()).thenReturn(authorList);
        when(authorMapper.toRecord(any(Author.class)))
                .thenReturn(new AuthorRecord(1L, "Autor 1", "autor1@email.com"))
                .thenReturn(new AuthorRecord(2L, "Autor 2", "autor2@email.com"));

        List<AuthorRecord> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(authorRepository).findAll();
    }

    @Test
    void testCreateAuthor_DeveSalvarERetornarAuthor() {
        AuthorRecord recordParaSalvar = new AuthorRecord(null, "Novo Autor", "novo@email.com");
        Author autorMapeado = new Author();
        Author autorSalvo = new Author(); // Simula a entidade após salvar (com ID)
        AuthorRecord recordEsperado = new AuthorRecord(1L, "Novo Autor", "novo@email.com");

        when(authorMapper.toEntity(recordParaSalvar)).thenReturn(autorMapeado);
        when(authorRepository.save(autorMapeado)).thenReturn(autorSalvo);
        when(authorMapper.toRecord(autorSalvo)).thenReturn(recordEsperado);

        AuthorRecord result = authorService.createAuthor(recordParaSalvar);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Novo Autor", result.name());
        verify(authorRepository).save(autorMapeado);
    }

    @Test
    void testUpdateAuthor_QuandoNaoEncontrado_DeveLancarExcecao() {
        AuthorRecord recordParaAtualizar = new AuthorRecord(99L, "Nome", "email@test.com");
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.updateAuthor(99L, recordParaAtualizar));
        verify(authorRepository).findById(99L);
        verify(authorRepository, never()).save(any()); // Garante que o save nunca foi chamado
    }

    @Test
    void testUpdateAuthor_QuandoEncontrado_DeveAtualizarERetornar() {

        AuthorRecord recordParaAtualizar = new AuthorRecord(1L, "Nome Atualizado", "email@atualizado.com");
        Author autorExistente = new Author(); // Objeto encontrado no banco
        Author autorSalvo = new Author(); // Objeto após o save
        AuthorRecord recordEsperado = new AuthorRecord(1L, "Nome Atualizado", "email@atualizado.com");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(autorExistente));
        when(authorRepository.save(autorExistente)).thenReturn(autorSalvo);
        when(authorMapper.toRecord(autorSalvo)).thenReturn(recordEsperado);

        AuthorRecord result = authorService.updateAuthor(1L, recordParaAtualizar);

        assertNotNull(result);
        assertEquals("Nome Atualizado", result.name());
        verify(authorRepository).findById(1L);
        verify(authorRepository).save(autorExistente);
    }

    @Test
    void testDeleteAuthor_QuandoNaoEncontrado_DeveLancarExcecao() {
        when(authorRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> authorService.deleteAuthor(99L));
        verify(authorRepository).existsById(99L);
        verify(authorRepository, never()).deleteById(anyLong()); // Garante que o delete não foi chamado
    }

    @Test
    void testDeleteAuthor_QuandoEncontrado_DeveDeletarComSucesso() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(authorRepository).deleteById(1L);

        assertDoesNotThrow(() -> authorService.deleteAuthor(1L));
        verify(authorRepository).existsById(1L);
        verify(authorRepository).deleteById(1L);
    }
}