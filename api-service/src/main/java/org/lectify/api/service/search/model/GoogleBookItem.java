package org.lectify.api.service.search.model;

import lombok.Data;

import java.util.List;

@Data
public class GoogleBookItem {

    private String kind;

    private String id;

    private VolumeInfo volumeInfo;


    @Data
    public static class VolumeInfo{

        private String title;

        private List<String> authors;

        private String publisher;

        private String publishedDate;

        private String description;

        private List<IndustryIdentifiers> industryIdentifiers;

        private int pageCount;

        private List<String> categories;

    }


    @Data
    public static class IndustryIdentifiers{

        private String type;

        private String identifier;
    }

}
