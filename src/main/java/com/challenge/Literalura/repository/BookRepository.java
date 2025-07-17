package com.challenge.Literalura.repository;

import com.challenge.Literalura.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByLanguageEquals(String lang);
}
