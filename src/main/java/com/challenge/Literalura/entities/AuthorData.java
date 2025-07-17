package com.challenge.Literalura.entities;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AuthorData(
        String name,
        @JsonAlias("birth_year") int birth_year,
        @JsonAlias("death_year") int death_year)  {
}
