package org.lectify.query.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.util.List;
import java.util.UUID;

@Document(collection = "Book")
@Data
public class Book {

    @Id
    private UUID bookId;

    @TextIndexed(weight = 5)
    private String title;

    @TextIndexed(weight = 4)
    private List<String> authors;

    @TextIndexed(weight = 2)
    private String publisher;

    private String publishedDate;

    @TextIndexed(weight = 1)
    private String description;

    private List<IndustryIdentifier> industryIdentifiers;

    @TextIndexed(weight = 3)
    private List<String> categories;

    private int pageCount;

    @TextScore
    private Float score;
}
