package com.challenge.Literalura.application;

import com.challenge.Literalura.entities.Author;
import com.challenge.Literalura.entities.AuthorData;
import com.challenge.Literalura.entities.Book;
import com.challenge.Literalura.entities.BookData;
import com.challenge.Literalura.repository.AuthorRepository;
import com.challenge.Literalura.repository.BookRepository;
import com.challenge.Literalura.service.APIConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Program {

    private APIConsumer consumo = new APIConsumer();
    private Scanner sc = new Scanner(System.in);
    private String adress = "https://gutendex.com/books/?search=";
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;


    public Program(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() throws JsonProcessingException {

        var option = -1;
        while (option != 0) {
            var menu = """
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados 
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            option = sc.nextInt();
            sc.nextLine();

            switch (option){
                case 1:
                    searchBookWeb();
                    break;
                case 2:
                    showAllRegisteredBooks();
                    break;
                case 3:
                    showAllRegisteredAuthors();
                    break;
                case 4:
                    showLivingAuthorsInAGivenYear();
                    break;
                case 5:
                    showBooksInAGivenLanguage();
                    break;
                case 0:
                    break;
            }


        }
    }

    private void searchBookWeb() throws JsonProcessingException {
        try {
            BookData dados = getDadosLivro();

            // Verifica se há autores
            if (dados.authors() == null || dados.authors().length == 0) {
                throw new NullPointerException("Nenhum autor encontrado para o livro.");
            }

            // Pega dados do autor
            AuthorData autorData = dados.authors()[0];

            // Busca o autor no banco
            Optional<Author> autorExistente = authorRepository.findByName(autorData.name());

            // Se não existir, cria e salva
            Author autor = autorExistente.orElseGet(() -> {
                Author novoAutor = new Author(
                        autorData.name(),
                        autorData.birth_year(),
                        autorData.death_year()
                );
                return authorRepository.save(novoAutor);
            });

            // Cria e salva o livro
            Book book = new Book(dados);
            book.setAuthor(autor); // Usa o autor existente ou recém-criado
            bookRepository.save(book);

            System.out.println("Livro salvo com sucesso.");

        } catch (NullPointerException e) {
            System.out.println("Erro: algum campo obrigatório está ausente. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado ao salvar o livro: " + e.getMessage());
        }
    }

    private BookData getDadosLivro() throws JsonProcessingException {
        System.out.println("Digite o nome do livro para busca:");
        var nomeLivro = sc.nextLine().replace(" ", "%20");
        var json = consumo.getData(adress + nomeLivro);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode firstResult = root.get("results").get(0);
        BookData bookData = mapper.treeToValue(firstResult, BookData.class);
        System.out.println();
        System.out.println("--------Livro Registrado--------");
        System.out.println("Título: " + bookData.title());
        for (AuthorData author : bookData.authors()) {
            System.out.println("Autor: " + author.name() + " (" + author.birth_year() + " - " + author.death_year() + ")");
        }
        for (String lang : bookData.languages()) {
            System.out.println("Idioma: " + lang);
        }
        System.out.println("Números de downloads: " + bookData.download_count());
        System.out.println("---------------------------------");
        System.out.println();
        return bookData;
    }

    private void showAllRegisteredBooks() {
        List<Book> books = bookRepository.findAll();
        books.stream().forEach(System.out::println);
    }

    private void showAllRegisteredAuthors() {
        List<Author> autores = authorRepository.findAll();
        autores.stream().forEach((System.out::println));
    }

    private void showLivingAuthorsInAGivenYear() {
        System.out.println("Insira o ano que deseja pesquisar: ");
        var year = sc.nextInt();

        List<Author> autoresVivos = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);

        if (autoresVivos.size() > 0) {
            autoresVivos.forEach(System.out::println);
        } else {
            System.out.println("-----------------------------------------------------------");
            System.out.println("Não há autores inseridos no banco vivos no ano inserido.");
            System.out.println("-----------------------------------------------------------");
        }
    }

    private void showBooksInAGivenLanguage() {
        System.out.println("Insira o idioma para realizar a busca: ");
        System.out.println("es - Espanhol");
        System.out.println("en - Inglês");
        System.out.println("fr - Francês");
        System.out.println("pt - Português");
        var lang = sc.next();

        List<Book> LivrosDeterminadoIdioma = bookRepository.findByLanguageEquals(lang);

        LivrosDeterminadoIdioma.forEach(System.out::println);
    }
}

