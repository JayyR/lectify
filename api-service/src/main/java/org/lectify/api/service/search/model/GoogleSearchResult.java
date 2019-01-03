package org.lectify.api.service.search.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoogleSearchResult {

    private int totalItems;

    private List<GoogleBookItem> items = new ArrayList<>();
}
