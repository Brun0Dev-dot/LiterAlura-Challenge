package com.challenge.Literalura.repository;

import com.challenge.Literalura.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);

    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(Integer year1, Integer year2);

}