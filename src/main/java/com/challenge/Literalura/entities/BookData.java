package com.challenge.Literalura.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(
        @JsonAlias("title") String title,
        @JsonAlias("authors") AuthorData[] authors,
        @JsonAlias("languages") String[] languages,
        @JsonAlias("download_count") int download_count
) {}