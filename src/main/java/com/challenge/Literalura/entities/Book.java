package com.challenge.Literalura.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private String language;

    private int downloadCount;

    public Book() {}

    public Book(String title, Author author, String language, int downloadCount) {
        this.title = title;
        this.author = author;
        this.language = language;
        this.downloadCount = downloadCount;
    }

    // Construtor que converte BookData para Book
    public Book(BookData dadosLivro) {
        this.title = dadosLivro.title();
        this.language = extractFirstOrDefault(dadosLivro.languages(), "desconhecido");
        this.downloadCount = dadosLivro.download_count();

        // Pega o primeiro autor, se existir
        if (dadosLivro.authors() != null && dadosLivro.authors().length > 0) {
            AuthorData autorData = dadosLivro.authors()[0];
            this.author = new Author(autorData.name(), autorData.birth_year(), autorData.death_year());
        } else {
            this.author = new Author("Autor Desconhecido", 0, 0);
        }

    }

    public Book(BookData dados, Author autor) {
        this.title = dados.title();
        this.language = extractFirstOrDefault(dados.languages(), "desconhecido");
        this.downloadCount = dados.download_count();
        this.author = autor;
    }

    private String extractFirstOrDefault(String[] array, String defaultValue) {
        return (array != null && array.length > 0) ? array[0] : defaultValue;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "--------- LIVRO ---------\n" +
                "Título: " + title + "\n" +
                "Autor: " + (author != null ? author.getName() : "Desconhecido") + "\n" +
                "Idioma: " + language + "\n" +
                "Número de downloads: " + downloadCount + "\n" +
                "-------------------------";
    }
}
