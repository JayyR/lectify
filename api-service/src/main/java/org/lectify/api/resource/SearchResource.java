package org.lectify.api.resource;

import org.lectify.common.value.book.BookItem;
import org.lectify.api.service.search.model.enums.SearchType;
import org.lectify.api.service.search.SearchService;
import org.lectify.api.service.search.model.enums.SearchBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/search")
public class SearchResource {

    private final SearchService searchService;

    @Autowired
    public SearchResource(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping()
    public Flux<BookItem> search(@RequestParam(value = "searchType",required = false,defaultValue = "EXTERNAL")SearchType searchType,
                                 @RequestParam(value = "searchBy",required = false,defaultValue = "TITLE")SearchBy searchBy,
                                 @RequestParam("param") String param){
        return searchService.bookSearch(searchType,searchBy,param);
    }
}
