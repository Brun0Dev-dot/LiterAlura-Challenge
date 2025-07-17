package com.challenge.Literalura;

import com.challenge.Literalura.application.Program;
import com.challenge.Literalura.entities.Author;
import com.challenge.Literalura.repository.AuthorRepository;
import com.challenge.Literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Program principal = new Program(bookRepository, authorRepository);
		principal.showMenu();
	}
}