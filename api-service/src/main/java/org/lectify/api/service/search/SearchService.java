package org.lectify.api.service.search;


import org.lectify.common.value.book.BookItem;
import org.lectify.api.service.search.model.enums.SearchBy;
import org.lectify.api.service.search.model.enums.SearchType;
import reactor.core.publisher.Flux;

public interface SearchService {

    Flux<BookItem> bookSearch(SearchType searchType, SearchBy searchBy, String parameter);
}
