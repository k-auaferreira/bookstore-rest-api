package br.com.fiap.bookstorerestapi.dto;

public record BookRecord(Long id, String title, String isbn, Long authorId) {
}
