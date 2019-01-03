package org.lectify.common.value.book;

import lombok.Data;

import java.util.List;

@Data
public class BookItem {

    private String title;

    private String subtitle;

    private List<String> authors;

    private String description;

    private List<String> categories;

    private String publisher;

    private int pageCount;

    private List<IndustryIdentifiers> industryIdentifiers;


    @Data
    private static class IndustryIdentifiers{

        private String type;

        private String identifier;
    }
}
