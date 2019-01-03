package org.lectify.api.service.search;

import org.lectify.common.value.book.BookItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface BookSearch {

    Mono<BookItem> isbnSearch(String isbn);

    Flux<BookItem> titleSearch(String title);

    Flux<BookItem> authorSearch(String author);

    default Flux<BookItem> search(String searchString){
        return Flux.empty();
    }

}
